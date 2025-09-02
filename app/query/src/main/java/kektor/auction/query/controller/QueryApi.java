package kektor.auction.query.controller;


import jakarta.validation.Valid;
import kektor.auction.query.dto.BidDto;
import kektor.auction.query.dto.CategoryDto;
import kektor.auction.query.dto.LotDto;
import kektor.auction.query.dto.filter.*;
import kektor.auction.query.service.QueryService;
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

    @GetMapping(path = "/bids")
    public Callable<List<BidDto>> getBids(@Valid BidRequestFilter filter,
//                                             @ValidSortingParameters(SortType.BID)
                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "creationTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(filter, pageable);
    }

    @GetMapping(path = "/bids", params = "paged")
    public Callable<Page<BidDto>> getPagedBids(@Valid BidRequestFilter requestFilter,
//                                           @ValidSortingParameters(SortType.BID)
                                               @PageableDefault(size = Integer.MAX_VALUE, sort = "creationTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/bids", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamBids(@Valid BidRequestFilter filter,
//                                                             @ValidSortingParameters(SortType.BID)
                                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "creationTime", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, filter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping(path = "/categories")
    public Callable<List<CategoryDto>> getCategories(@Valid CategoryRequestFilter requestFilter,
//                                                               @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
                                                     @PageableDefault(size = Integer.MAX_VALUE, sort = "name") Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }

    @GetMapping(path = "/categories", params = "paged")
    public Callable<Page<CategoryDto>> getPagedCategories(@Valid CategoryRequestFilter requestFilter,
//                                                          @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
                                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "name") Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamCategories(@Valid CategoryRequestFilter requestFilter,
//                                                                   @ValidSortingParameters(ValidSortingParameters.SortType.CATEGORY)
                                                                   @PageableDefault(size = Integer.MAX_VALUE, sort = "name") Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping(path = "/lots")
    public Callable<List<LotDto>> getLots(@Valid LotRequestFilter requestFilter,
//                                                    @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                         @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }


    @GetMapping(path = "/lots", params = "paged")
    public Callable<Page<LotDto>> getLotsPaged(@Valid LotRequestFilter requestFilter,
//                                                      @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                           @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/lots",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamLots(@Valid LotRequestFilter requestFilter,
//                                                         @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                         @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping(path = "/operations")
    public Callable<List<LotDto>> getOperations(@Valid BalanceOpRequestFilter requestFilter,
//                                                    @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }


    @GetMapping(path = "/operations", params = "paged")
    public Callable<Page<LotDto>> getPagedOperations(@Valid BalanceOpRequestFilter requestFilter,
//                                                      @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                               @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/operations",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamOperations(@Valid BalanceOpRequestFilter requestFilter,
//                                                         @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                          @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }


    @GetMapping(path = "/accounts")
    public Callable<List<LotDto>> getAccounts(@Valid PaymentAccountFilter requestFilter,
//                                                    @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }


    @GetMapping(path = "/accounts", params = "paged")
    public Callable<Page<LotDto>> getPagedAccounts(@Valid PaymentAccountFilter requestFilter,
//                                                      @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                     @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/accounts",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamAccounts(@Valid PaymentAccountFilter requestFilter,
//                                                         @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                                @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }

    @GetMapping(path = "/sagas")
    public Callable<List<LotDto>> getSagas(@Valid SagaRequestFilter requestFilter,
//                                                    @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                              @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getAll(requestFilter, pageable);
    }


    @GetMapping(path = "/sagas", params = "paged")
    public Callable<Page<LotDto>> getPagedSagas(@Valid SagaRequestFilter requestFilter,
//                                                      @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                   @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return () -> queryService.getPage(requestFilter, pageable);
    }

    @GetMapping(path = "/sagas",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> streamSagas(@Valid SagaRequestFilter requestFilter,
//                                                         @ValidSortingParameters(ValidSortingParameters.SortType.ITEM)
                                                              @PageableDefault(size = Integer.MAX_VALUE, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var emitter = new ResponseBodyEmitter(-1L);
        queryService.streamAll(emitter, requestFilter, pageable);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_NDJSON).
                body(emitter);
    }
}
