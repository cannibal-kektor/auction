package kektor.auction.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import kektor.auction.category.validation.Validate;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

//@Value
//@NonFinal
@Builder
@Jacksonized
//@EqualsAndHashCode(cacheStrategy = LAZY)
public record CategoryDto(
        @Positive(groups = Validate.IfUpdate.class)
//        @NotNull(groups = Validate.IfUpdate.class)
        @Null(groups = Validate.IfCreate.class)
        Long id,

        @Positive
        Long parentId,

        @NotBlank
        @Size(min = 4, max = 20)
        String name
) {
}
