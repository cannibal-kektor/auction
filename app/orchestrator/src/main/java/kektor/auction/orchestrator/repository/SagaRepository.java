package kektor.auction.orchestrator.repository;

import kektor.auction.orchestrator.model.Saga;
import kektor.auction.orchestrator.model.SagaStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Long> {

    boolean existsByLotIdAndStatusAndCreatedOnIsBefore(Long lotId, SagaStatus status, Instant createdOn);

    List<Saga> findStalledByStatusAndCreatedOnIsBefore(SagaStatus status, Instant createdOn);

    @Query("select s.status from Saga s where s.sagaId= :id")
    SagaStatus findSagaStatusByLotId(@Param("id") Long id);
}
