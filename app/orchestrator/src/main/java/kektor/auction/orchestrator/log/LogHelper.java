package kektor.auction.orchestrator.log;

import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.service.SagaPhase;
import kektor.auction.orchestrator.service.step.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogHelper {

    public static final String EXECUTION_PHASE_EXCEPTION_LOG = "Execution phase exception. Message:[{}] Saga:[{}]";
    public static final String COMMIT_PHASE_EXCEPTION_LOG = "Commit phase exception. Message:[{}] Saga:[{}]";
    public static final String COMPENSATE_PHASE_EXCEPTION_LOG = "Compensation phase exception. Message:[{}] Saga:[{}]";

    public static final String BID_CREATE_EXECUTION_EXCEPTION_LOG = "Bid creation Saga step: Execution phase exception. Message:[{}] Saga:[{}]";
    public static final String BID_CREATE_COMMIT_EXCEPTION_LOG = "Bid creation Saga step: Commit phase exception: Message:[{}] Saga:[{}]";
    public static final String BID_CREATE_COMPENSATE_EXCEPTION_LOG = "Bid creation Saga step: Compensation phase exception: Message:[{}] Saga:[{}]";

    public static final String LOT_UPDATE_EXECUTION_EXCEPTION_LOG = "Lot update Saga step: Execution phase exception. Message:[{}] Saga:[{}]";
    public static final String LOT_UPDATE_COMPENSATE_EXCEPTION_LOG = "Lot update Saga step: Compensation phase exception. Message:[{}] Saga:[{}]";

    public static final String ERROR_FORWARDING_STALLED_COMPENSATION = "Error while forwarding saga compensation to broker. Manual intervention is required. Message:[{}] Saga:[{}]";
    public static final String ATTEMPT_TO_RERUN_STALLED_COMPENSATION = "Starting attempt to resolve stalled saga. Rerunning compensate steps. Attempt:[{}] Saga:[{}]";
    public static final String FAILED_ATTEMPT_TO_RERUN_STALLED_COMPENSATION = "Failed attempt to resolve stalled saga. Message:[{}] Saga:[{}]";
    public static final String MANUAL_INTERVENTION_MAYBE_REQUIRED = "Manual intervention maybe required to resolve stalled saga. Message:[{}] Saga:[{}]";


    public void logPhaseException(SagaPhase value, Saga saga, Throwable ex) {
        String msg = switch (value) {
            case EXECUTE -> EXECUTION_PHASE_EXCEPTION_LOG;
            case COMMIT -> COMMIT_PHASE_EXCEPTION_LOG;
            case COMPENSATE -> COMPENSATE_PHASE_EXCEPTION_LOG;
        };
        log.atError()
                .setMessage(msg)
                .addArgument(ex.getMessage())
                .addArgument(saga)
                .log();
    }


    public <T extends SagaStep> void logBidCreateStepException(SagaPhase phase, Saga saga, Throwable ex) {
        String msg = switch (phase){
            case EXECUTE -> BID_CREATE_EXECUTION_EXCEPTION_LOG;
            case COMMIT -> BID_CREATE_COMMIT_EXCEPTION_LOG;
            case COMPENSATE -> BID_CREATE_COMPENSATE_EXCEPTION_LOG;
        };
        log.atError()
                .setMessage(msg)
                .addArgument(ex.getMessage())
                .addArgument(saga)
                .log();
    }


    public <T extends SagaStep> void logLotUpdateStepException(SagaPhase phase, Saga saga, Throwable ex) {
        String msg = switch (phase){
            case EXECUTE -> LOT_UPDATE_EXECUTION_EXCEPTION_LOG;
            case COMPENSATE -> LOT_UPDATE_COMPENSATE_EXCEPTION_LOG;
            default -> throw new IllegalStateException("Unexpected value: " + phase) ;
        };
        log.atError()
                .setMessage(msg)
                .addArgument(ex.getMessage())
                .addArgument(saga)
                .log();
    }

    public void logErrorForwardingStalledCompensation(Saga saga, Throwable ex) {
        log.atError()
                .setMessage(ERROR_FORWARDING_STALLED_COMPENSATION)
                .addArgument(ex.getMessage())
                .addArgument(saga)
                .log();
    }

    public void logAttemptToResolveStalledSaga(Saga stalledSaga, Integer attempt) {
        log.atError()
                .setMessage(ATTEMPT_TO_RERUN_STALLED_COMPENSATION)
                .addArgument(attempt==null ? "initial delivery" : attempt.toString())
                .addArgument(stalledSaga)
                .log();
    }

    public void logFailedAttemptResolvingStalledCompensation(Saga saga, Throwable ex) {
        log.atError()
                .setMessage(FAILED_ATTEMPT_TO_RERUN_STALLED_COMPENSATION)
                .addArgument(ex.getMessage())
                .addArgument(saga)
                .log();
    }

    public void logManualInterventionMaybeRequired(Saga saga, String error) {
        log.atError()
                .setMessage(MANUAL_INTERVENTION_MAYBE_REQUIRED)
                .addArgument(error)
                .addArgument(saga)
                .log();
    }
}
