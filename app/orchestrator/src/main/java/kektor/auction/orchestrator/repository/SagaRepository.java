package kektor.auction.orchestrator.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.model.SagaStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Long> {

    @QueryHints(
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "2000"
            ))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Saga> findStalledByStatusAndCreationTimeIsBefore(SagaStatus status, Instant creationTime);

}
