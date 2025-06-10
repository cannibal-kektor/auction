package kektor.auction.lot.repository;


import kektor.auction.lot.model.Lot;

public interface CustomOperation {
    Lot saveNew(Lot entity);

    Lot findExceptionally(Long id);

}
