package kektor.auction.orchestrator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Data
@Table(name = "saga",
        uniqueConstraints =
        @UniqueConstraint(name = "idx_saga_lot_unique ", columnNames = {"lot_id","lot_version"}))
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
    @Enumerated(EnumType.STRING)
    SagaStatus status = SagaStatus.ACTIVE;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long lotId;

    @NotNull
    @PositiveOrZero
    @Column(updatable = false)
    Long lotVersion;

    @NotNull
    @Column(updatable = false)
    Instant createdOn;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long bidderId;

    @NotNull
    @Positive
    @Column(updatable = false)
    BigDecimal newBidAmount;

    @NotNull
    @Positive
    @Column(updatable = false)
    BigDecimal compensateBidAmount;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long compensateWinningBidId;


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