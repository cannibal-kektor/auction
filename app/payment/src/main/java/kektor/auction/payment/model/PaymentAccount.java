package kektor.auction.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "payment_accounts",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_account ", columnNames = {"user_id"}))
@Getter
@Setter
public class PaymentAccount extends IdEntity {

    @DecimalMin("0")
    @Column(nullable = false, precision = 19, scale = 4)
    BigDecimal balance = BigDecimal.ZERO;

    @NotNull
    @Version
    Long version;

    @Positive
    @NotNull
    @Column(updatable = false)
    Long userId;

    @CreationTimestamp(source = SourceType.DB)
    @Column(nullable = false, updatable = false)
    Instant registrationDate;

    @OneToMany(
            mappedBy = "paymentAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("timestamp DESC")
    private Set<BalanceOperation> operations = new LinkedHashSet<>();


    public void addOperation(BalanceOperation operation) {
        operations.add(operation);
        operation.setPaymentAccount(this);
    }

    public void removeOperation(BalanceOperation operation) {
        operations.remove(operation);
        operation.setPaymentAccount(null);
    }

}
