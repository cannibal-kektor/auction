package kektor.auction.orchestrator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "SAGA")
@Data
public class Saga {

    public static final String ID_GENERATOR = "ID_GENERATOR";
    public static final String ID_GENERATOR_SEQUENCE_NAME = "ID_SEQUENCE_GENERATOR";

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    @SequenceGenerator(name = ID_GENERATOR,
            sequenceName = ID_GENERATOR_SEQUENCE_NAME,
            allocationSize = 100,
            initialValue = 1000
    )
    Long sagaId;

    @NotNull
    @Column(length = 20)
    @Enumerated(value = EnumType.STRING)
    SagaStatus status = SagaStatus.ACTIVE;

    @NotNull
    @Positive
    Long lotId;

    @NotNull
    @PositiveOrZero
    Long itemVersion;


    @NotNull
    Instant createdOn;

    @NotNull
    @Positive
    Long bidderId;

    @NotNull
    boolean bidStarted = false;

    @NotNull
    boolean paymentStarted = false;

    @NotNull
    boolean lotChangeStarted = false;

    @NotNull
    @Positive
    BigDecimal newBidAmount;

    @NotNull
    @Positive
    BigDecimal compensateHighestBid;

    @NotNull
    Long compensateBidsCount = 0L;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Saga that = (Saga) o;
        return getSagaId().equals(that.getSagaId());
    }

    @Override
    public int hashCode() {
        return getSagaId().hashCode();
    }
}