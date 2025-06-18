package kektor.auction.category.exception;


import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Category not found by identifier = %d";

    long categoryId;

    public CategoryNotFoundException(long categoryId) {
        super(String.format(NOT_FOUND, categoryId));
        this.categoryId = categoryId;
    }
}
