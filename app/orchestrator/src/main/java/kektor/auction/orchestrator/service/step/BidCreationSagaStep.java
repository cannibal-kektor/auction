package kektor.auction.orchestrator.service.step;


import kektor.auction.orchestrator.dto.BidDto;
import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.log.LogHelper;
import kektor.auction.orchestrator.service.SagaPhase;
import kektor.auction.orchestrator.mapper.SagaMapper;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.client.BidServiceClient;
import kektor.auction.orchestrator.service.client.LotServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class BidCreationSagaStep implements SagaStep {

    final BidServiceClient bidService;
    final LotServiceClient lotService;
    final SagaMapper sagaMapper;
    final LogHelper logHelper;

    Saga saga;

    @Override
    public SagaStep setSaga(Saga saga) {
        this.saga = saga;
        return this;
    }

    @Override
    public void execute() {
        var newBid = sagaMapper.toBid(saga);
        bidService.createBid(newBid);
    }

    @Override
    public void commit() {
        bidService.commitBid(saga.getSagaId());
    }

    @Override
    public void compensate() {
        Long sagaId = saga.getSagaId();
        Long lotId = saga.getLotId();
        Optional<BidDto> bidOpt = bidService.fetchBid(sagaId);

        if (bidOpt.isPresent()) {
            var bid = bidOpt.get();
            bidService.rejectBid(sagaId);
            LotDto lot = lotService.fetchLot(lotId);

            if (lot.winningBidId().equals(saga.getCompensateWinningBidId()))
                return;

            if (lot.winningBidId().equals(bid.id()))
                lotService.updateBidInfo(lotId, lot.version(),
                        saga.getCompensateBidAmount(), saga.getCompensateWinningBidId(), true);
        }
    }

    @SneakyThrows
    @Override
    public Void handleExecutionException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.EXECUTE, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCommitException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.COMMIT, saga, ex);
        throw ex;
    }

    @SneakyThrows
    @Override
    public Void handleCompensateException(Throwable ex) {
        logHelper.logBidCreateStepException(SagaPhase.COMPENSATE, saga, ex);
        throw ex;
    }
}
