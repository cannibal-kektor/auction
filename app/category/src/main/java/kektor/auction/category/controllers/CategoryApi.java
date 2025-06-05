package kektor.auction.category.controllers;

import jakarta.validation.constraints.Positive;
import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryApi {

    final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public Callable<CategoryDto> get(@PathVariable("categoryId") @Positive Long categoryId) {
        return () -> categoryService.get(categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<CategoryDto> create(@RequestBody @Validated CategoryDto.Request createDTO) {
        return () -> categoryService.create(createDTO);
    }

    @PutMapping(path = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<CategoryDto> update(
            @PathVariable("categoryId") @Positive Long categoryId,
            @RequestBody @Validated CategoryDto.Request updateDTO) {
        return () -> categoryService.update(categoryId, updateDTO);
    }

}
