package kektor.auction.bid.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "bid",
        uniqueConstraints = @UniqueConstraint(name = "uk_saga_id", columnNames = {"saga_id"}))
@Getter
@Setter
public class Bid {

    public static final String ID_GENERATOR = "ID_GENERATOR";
    public static final String ID_GENERATOR_SEQUENCE_NAME = "ID_SEQUENCE_GENERATOR";

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    @SequenceGenerator(name = ID_GENERATOR,
            sequenceName = ID_GENERATOR_SEQUENCE_NAME,
            allocationSize = 100,
            initialValue = 1000
    )
    Long id;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long lotId;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long bidderId;

    @NotNull
    @Positive
    @Column(updatable = false)
    Long sagaId;

    @NotNull
    @Positive
    @Column(updatable = false)
    BigDecimal amount;

    @NotNull
    @Column(updatable = false)
    Instant createdOn;

    @NotNull
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    BidStatus status = BidStatus.PENDING;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Bid that = (Bid) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}