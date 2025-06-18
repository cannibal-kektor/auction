package kektor.auction.category.dto;

import jakarta.validation.constraints.*;
import kektor.auction.category.validation.Validate;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record CategoryDto(

        @Positive(groups = Validate.IfUpdate.class)
        @Null(groups = Validate.IfCreate.class)
        Long id,

        @Positive
        Long parentId,

        @NotBlank
        @Size(min = 4, max = 20)
        String name

) {
}
