package kektor.auction.orchestrator.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.model.SagaStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Long> {

    boolean existsByLotIdAndStatusAndCreatedOnIsBefore(Long lotId, SagaStatus status, Instant createdOn);

    List<Saga> findStalledByStatusAndCreatedOnIsBefore(SagaStatus status, Instant createdOn);
}
