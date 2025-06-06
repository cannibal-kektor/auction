package kektor.auction.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import kektor.auction.category.validation.Validate;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@NonFinal
@Builder
@Jacksonized
//@EqualsAndHashCode(cacheStrategy = LAZY)
public class CategoryDto {

    @Positive(groups = Validate.IfUpdate.class)
    Long id;

    @Positive
    Long parentId;

    @NotBlank
    @Size(min = 4, max = 20)
    String name;

}
