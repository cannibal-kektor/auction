package kektor.auction.payment.repository;

import kektor.auction.payment.model.BalanceOperation;
import kektor.auction.payment.model.CreditOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BalanceOperationRepository extends JpaRepository<BalanceOperation, Long> {

    @Query("SELECT c FROM CreditOperation c WHERE c.sagaId = :sagaId")
    Optional<CreditOperation> findCreditOperationBySagaId(@Param("sagaId") Long sagaId);

    @Query("SELECT c FROM CreditOperation c left join fetch c.paymentAccount WHERE c.sagaId = :sagaId")
    Optional<CreditOperation> findCreditOperationBySagaIdFetchAccount(@Param("sagaId") Long sagaId);

}
