package kektor.auction.payment.repository;

import kektor.auction.payment.model.PaymentAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {

    Optional<PaymentAccount> findByUserId(Long userId);

    @EntityGraph(attributePaths = "operations", type = EntityGraph.EntityGraphType.LOAD)
    Optional<PaymentAccount> findWithOperationsById(Long id);

}
