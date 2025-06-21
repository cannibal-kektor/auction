package kektor.auction.payment.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "debet_operations")
@PrimaryKeyJoinColumn(name = "operation_id")
@DiscriminatorValue("DEBIT")
public class DebitOperation extends BalanceOperation {

}
