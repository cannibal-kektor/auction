package kektor.auction.lot.service;

import kektor.auction.lot.dto.LotCreateDto;
import kektor.auction.lot.dto.LotFetchDto;
import kektor.auction.lot.dto.LotUpdateDto;
import kektor.auction.lot.mapper.LotMapper;
import kektor.auction.lot.model.Lot;
import kektor.auction.lot.repository.LotRepository;
import kektor.auction.lot.exception.AuctionAlreadyStartedException;
import kektor.auction.lot.exception.StaleItemVersionException;
import kektor.auction.lot.stream.LotChangedEvent;
import kektor.auction.lot.stream.StreamResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LotService {

    final LotRepository lotRepository;
    final StreamResponseHelper streamResponseHelper;
    final ApplicationEventPublisher eventPublisher;
    final LotMapper mapper;
    final CategoryCacheService categoryService;


    public LotFetchDto get(Long id) {
        var lot = lotRepository.findExceptionally(id);
        var categories = categoryService.getCategories(lot.getCategoriesId());
        return mapper.toDto(lot, categories);
    }

    @Transactional
    public LotFetchDto create(LotCreateDto createDTO) {
        Lot lot = mapper.toModel(createDTO);
        lot = lotRepository.saveNew(lot);
        var categories = categoryService.getCategories(lot.getCategoriesId());
        //Делает отдельный инсерт на каждую категорию(смотри апдейт)
        return mapper.toDto(lot, categories);
    }

    //TODO AOP Check exceptions in @Transactional?
    @Transactional
    public LotFetchDto update(LotUpdateDto dto) {
        Long lotId = dto.id();
        Long updV = dto.version();
        Lot lot = lotRepository.findExceptionally(lotId);
        Long currentV = lot.getVersion();
        if (!currentV.equals(updV)) {
            throw new StaleItemVersionException(lot.getId(), currentV, updV);
        }

        if (lot.getAuctionStart().isBefore(Instant.now())) {
            throw new AuctionAlreadyStartedException(lotId, lot.getAuctionStart());
        }
        mapper.update(dto, lot);
        //Делает отдельный инсерт на каждую категорию в новой коллекции на апдейт(Походу в промежуточных таблицах ManyToMany Hibernate не делает(не может?) одну батч вставку в category_item)
        //batch_size не помог
        //https://stackoverflow.com/questions/65107454/hibernate-many-to-many-collection-insertion-optimization
        //
        lotRepository.flush();
        //MappingJacksonValue set serialization view
        var categories = categoryService.getCategories(lot.getCategoriesId());
        LotFetchDto lotDto = mapper.toDto(lot, categories);
        eventPublisher.publishEvent(new LotChangedEvent.LotUpdated(lotDto));
        return lotDto;
    }

    @Async
    public void subscribeSseEmitter(Long lotId, Long lotVersion, SseEmitter sseEmitter) {
        Lot lot = lotRepository.findExceptionally(lotId);
        if (!lotVersion.equals(lot.getVersion())) {
            sseEmitter.completeWithError(new StaleItemVersionException(lotId, lot.getVersion(), lotVersion));
            return;
        }
        streamResponseHelper.registerSseEmitter(lotId, sseEmitter);
    }

}
