package kektor.auction.category.controllers;

import jakarta.validation.constraints.Positive;
import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.service.CategoryService;
import kektor.auction.category.validation.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryApi {

    final CategoryService categoryService;

    @GetMapping("/{id}")
    public Callable<CategoryDto> get(@PathVariable("id") @Positive Long categoryId) {
        return () -> categoryService.get(categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<CategoryDto> create(@RequestBody @Validated(Validate.Create.class) CategoryDto createDTO) {
        return () -> categoryService.create(createDTO);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callable<CategoryDto> update(
            @PathVariable("id") @Positive Long categoryId,
            @RequestBody @Validated(Validate.Update.class) CategoryDto updateDTO) {
        return () -> categoryService.update(categoryId, updateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Callable<Void> delete(@PathVariable("id") @Positive Long categoryId) {
        return () -> {
            categoryService.delete(categoryId);
            return null;
        };
    }

    @GetMapping
    public Callable<List<CategoryDto>> getAll() {
        return categoryService::getAll;
    }

    @GetMapping("/bulk")
    public Callable<List<CategoryDto>> getBulk(List<Long> categoryIds) {
        return () -> categoryService.getBulk(categoryIds);
    }
}
