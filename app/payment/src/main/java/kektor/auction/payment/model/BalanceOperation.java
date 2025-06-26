package kektor.auction.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "balance_operations",
        uniqueConstraints = @UniqueConstraint(name = "uk_saga_id", columnNames = {"saga_id"}))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "operation_type", discriminatorType = DiscriminatorType.STRING)
public abstract class BalanceOperation extends IdEntity {

    @DecimalMin(value = "0.01")
    @Column(nullable = false, updatable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @CreationTimestamp(source = SourceType.DB)
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private PaymentAccount paymentAccount;

}
