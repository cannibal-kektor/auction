package kektor.auction.lot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "lot")
@Check(
        constraints = "auction_start < auction_end"
)
//@Check(
//        constraints = "AUCTIONEND > CURRENT_TIMESTAMP "
//)
@Getter
@Setter
@NoArgsConstructor
public class Lot {

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

    //TODO AuctionStart and logic

    @NotNull
    @Version
    Long version;

    @NotBlank
    @Size(min = 2, max = 255)
    String name;

    @NotBlank
    @Size(min = 10, max = 4000)
    String description;

    @NotNull
    @Column(updatable = false)
    Long sellerId;

    @NotNull
    @DecimalMin("0")
    BigDecimal initialPrice = new BigDecimal(0);

    @NotNull
    @Future(message = "{Item.auctionStart.Future}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant auctionStart;

    @NotNull
    @Future(message = "{Item.auctionEnd.Future}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant auctionEnd;

    @ElementCollection
    @CollectionTable(
            name = "lot_categories",
            joinColumns = @JoinColumn(name = "lot_id"),
            foreignKey = @ForeignKey(name = "lot_categories_foreign_key",
                    value = ConstraintMode.CONSTRAINT),
            indexes = @Index(
                    name = "idx_lot_categories",
                    columnList = "categories_id"
            )
    )
    protected Set<@Positive Long> categoriesId = new HashSet<>();

    @Embedded
    LotStat lotStat = new LotStat();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Lot that = (Lot) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}


