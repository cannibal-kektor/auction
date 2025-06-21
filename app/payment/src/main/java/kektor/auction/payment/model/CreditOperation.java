package kektor.auction.payment.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "credit_operations",
        uniqueConstraints = @UniqueConstraint(name = "uk_sagaId", columnNames = {"sagaId"}))
@PrimaryKeyJoinColumn(name = "operation_id")
@DiscriminatorValue("CREDIT")
public class CreditOperation extends BalanceOperation {

    public enum Status {
        RESERVED, ACCEPTED, CANCELLED
    }

    @NotNull
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status = Status.RESERVED;

    @Positive
    private Long bidId;

    @Positive
    @NotNull
    @Column(updatable = false)
    private Long sagaId;


}
