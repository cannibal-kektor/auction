package kektor.auction.category.exception;


import lombok.Getter;

@Getter
public class RestrictParentDeletionException extends RuntimeException {

    private static final String PARENT_DELETION_RESTRICT = "Cannot delete category with existing sub-categories [parent id = %d]";

    long parentId;

    public RestrictParentDeletionException(long parentId) {
        super(String.format(PARENT_DELETION_RESTRICT, parentId));
        this.parentId = parentId;
    }
}
