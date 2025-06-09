package kektor.auction.category.exception;


import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "[%s] resource not found by identifier = %d";

    long resourceId;
    Class<?> resourceClass;

    public ResourceNotFoundException(Class<?> resourceClass, long resourceId) {
        super(String.format(NOT_FOUND, resourceClass.getSimpleName(), resourceId));
        this.resourceClass = resourceClass;
        this.resourceId = resourceId;
    }
}
