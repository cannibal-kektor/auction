package kektor.auction.lot.service;

import jakarta.persistence.OptimisticLockException;
import kektor.auction.lot.dto.*;
import kektor.auction.lot.dto.msg.*;
import kektor.auction.lot.mapper.LotMapper;
import kektor.auction.lot.model.Lot;
import kektor.auction.lot.model.LotStat;
import kektor.auction.lot.repository.LotRepository;
import kektor.auction.lot.exception.AuctionAlreadyStartedException;
import kektor.auction.lot.exception.StaleLotVersionException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Transactional(readOnly = true, timeout = 10000000)
@RequiredArgsConstructor
public class LotService {

    final LotRepository lotRepository;
    final ApplicationEventPublisher eventPublisher;
    final LotMapper mapper;
    final CategoryCacheService categoryService;


    public LotDto get(Long id) {
        var lot = lotRepository.findExceptionally(id);
        var categories = categoryService.getCategories(lot.getCategoriesId());
        return mapper.toDto(lot, categories);
    }

    public Long getCurrentVersion(Long id) {
        return lotRepository.findExceptionally(id).getVersion();
    }

    @Transactional
    public LotDto create(LotCreateDto createDTO) {
        Lot lot = mapper.toModel(createDTO);
        lot = lotRepository.saveNew(lot);
        var categories = categoryService.getCategories(lot.getCategoriesId());
        return mapper.toDto(lot, categories);
    }
    //TODO AOP Check exceptions in @Transactional?

    @Transactional
    public LotDto update(LotUpdateDto dto) {
        Lot lot = lotRepository.findExceptionally(dto.id());
        validateLotUpdate(lot, dto);
        mapper.update(dto, lot);
        try {
            lotRepository.flush();
        } catch (OptimisticLockException e) {
            Long currentV = lotRepository.fetchLotVersion(dto.id());
            throw new StaleLotVersionException(lot.getId(), currentV, lot.getVersion());
        }
        var categories = categoryService.getCategories(lot.getCategoriesId());
        LotDto lotDto = mapper.toDto(lot, categories);
        eventPublisher.publishEvent(new LotInfoUpdateMessage(now(), lotDto));
        return lotDto;
    }

    @Transactional
    public void updateHighestBid(Long id, Long version, BigDecimal highestBid,
                                 Long winningBidId, boolean isRollback) {
        Lot lot = lotRepository.findExceptionally(id);
        Long currentV = lot.getVersion();
        if (!currentV.equals(version)) {
            throw new StaleLotVersionException(lot.getId(), currentV, version);
        }
        LotStat lotStat = lot.getLotStat();
        lotStat.setHighestBid(highestBid);
        lotStat.setWinningBidId(winningBidId);
        Long currentBidsCount = lotStat.getBidsCount();
        lotStat.setBidsCount(isRollback ? currentBidsCount - 1 : currentBidsCount + 1);

        try {
            lotRepository.flush();
        } catch (OptimisticLockException e) {
            currentV = lotRepository.fetchLotVersion(id);
            throw new StaleLotVersionException(lot.getId(), currentV, lot.getVersion());
        }

        eventPublisher.publishEvent(new LotBidInfoUpdateMessage(now(), id, currentV,
                highestBid, winningBidId, isRollback));
    }


    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void activateRipeLots() {
        Instant now = now();
        List<Lot> lots = lotRepository.findTop10RipeLotsByStatusAndAuctionStartAfter(Lot.Status.PENDING, now);
        lots.forEach(lot -> lot.setStatus(Lot.Status.ACTIVE));
        lotRepository.flush();
        lots.forEach(lot -> eventPublisher.publishEvent(
                new LotStatusUpdateMessage(now(), lot.getId(), lot.getStatus())));
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void completeFinishedLots() {
        Instant nowWithSmallDelay = now().plus(5, SECONDS);
        List<Lot> lots = lotRepository.findTop10RipeLotsByStatusAndAuctionEndBefore(Lot.Status.PENDING, nowWithSmallDelay);
        lots.forEach(lot -> lot.setStatus(Lot.Status.COMPLETED));
        lotRepository.flush();
        lots.forEach(lot -> eventPublisher.publishEvent(
                new LotStatusUpdateMessage(now(), lot.getId(), lot.getStatus())));
    }


    public void validateLotUpdate(Lot lot, LotUpdateDto dto) {
        Long currentV = lot.getVersion();
        Long updV = dto.version();
        Long lotId = dto.id();
        Instant auctionStart = lot.getAuctionStart();
        if (!currentV.equals(updV)) {
            throw new StaleLotVersionException(lotId, currentV, updV);
        }
        if (auctionStart.isBefore(now())) {
            throw new AuctionAlreadyStartedException(lotId, auctionStart);
        }
    }

    public void notifyLotUpdateSubscribers(Long categoryId, CategoryEventMessage message) {
        lotRepository.findLotsIdsByCategory(categoryId)
                .stream()
                .map(lotId -> new LotCategoriesUpdateMessage(now(), lotId, message))
                .forEach(eventPublisher::publishEvent);
    }
}
