package kektor.auction.query.controller;


import jakarta.validation.Valid;
import kektor.auction.query.dto.BidDto;
import kektor.auction.query.dto.filter.BidRequestFilter;
import kektor.auction.query.dto.filter.CategoryRequestFilter;
import kektor.auction.query.dto.filter.LotRequestFilter;
import kektor.auction.query.service.QueryService;
import kektor.auction.query.validation.ValidSortingParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.concurrent.Callable;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api"
        , consumes = MediaType.APPLICATION_JSON_VALUE
        , produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryApi {

    final QueryService queryService;


    @GetMapping
    public Callable<List<BidDto>> getAllBids(@Valid BidRequestFilter filter,
                                             @ValidSortingParameters(SortType.BID)
//                                                       Pageable pageable) {
                                                       @PageableDefault(size = Integer.MAX_VALUE, sort = "amount", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(filter, pageable);
    }

    @GetMapping(params = "paged")
    public Callable<Page<BidDto>> getPaged(@Valid BidRequestFilter requestFilter,
                                                     @ValidSortingParameters(SortType.BID)
//                                                     Pageable pageable) {
                                                     @PageableDefault(size = Integer.MAX_VALUE, sort = "amount", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamAllBids(@Valid BidRequestFilter filter,
                                                             @ValidSortingParameters(SortType.BID)
//                                                              Pageable pageable) {
                                                             @PageableDefault(size = Integer.MAX_VALUE, sort = "amount", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, filter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping
    public Callable<List<? extends CategoryDTO>> getAll(@Valid CategoryRequestFilter requestFilter,
                                                        @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
//                                                        Pageable pageable) {
                                                        @PageableDefault(size = Integer.MAX_VALUE, sort = "id") Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }

    @GetMapping(params = "paged")
    public Callable<Page<? extends CategoryDTO>> getPaged(@Valid CategoryRequestFilter requestFilter,
                                                          @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
//                                                          Pageable pageable) {
                                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "id") Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamAllCategories(@Valid CategoryRequestFilter requestFilter,
                                                                   @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
//                                                                   Pageable pageable) {
                                                                   @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping
    public Callable<List<? extends ItemDTO>> getAll(@Valid LotRequestFilter requestFilter,
                                                    @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
//                                                    Pageable pageable) {
                                                    @PageableDefault(size = Integer.MAX_VALUE, sort = "id") Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }


    @GetMapping(params = "paged")
    public Callable<Page<? extends ItemDTO>> getPaged(@Valid LotRequestFilter requestFilter,
                                                      @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
//                                                      Pageable pageable) {
                                                      @PageableDefault(size = Integer.MAX_VALUE, sort = "id") Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamAll(@Valid LotRequestFilter requestFilter,
                                                         @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
//                                                         Pageable pageable) {
                                                         @PageableDefault(size = Integer.MAX_VALUE, sort = "id") Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

}
