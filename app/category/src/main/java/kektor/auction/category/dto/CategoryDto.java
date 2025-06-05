package kektor.auction.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@NonFinal
@SuperBuilder
@Jacksonized
//@EqualsAndHashCode(cacheStrategy = LAZY)
public class CategoryDto {

    @Positive
    Long id;

    @JsonInclude
    Long parentId;

    String name;

    public record Request(

            @Positive
            Long parentId,

            @NotBlank
            @Size(min = 4, max = 20)
            String name
    ) {
    }



}
