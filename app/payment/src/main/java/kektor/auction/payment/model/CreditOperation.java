package kektor.auction.payment.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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
