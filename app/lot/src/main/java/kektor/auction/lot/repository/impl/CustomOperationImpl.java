package kektor.auction.lot.repository.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kektor.auction.lot.model.Lot;
import kektor.auction.lot.repository.CustomOperation;
import kektor.auction.lot.exception.LotNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomOperationImpl implements CustomOperation {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Lot saveNew(Lot entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Lot findExceptionally(Long id) {
        Lot entity = entityManager.find(Lot.class, id);
        if (entity == null) {
            throw new LotNotFoundException(id);
        }
        return entity;
    }

}
