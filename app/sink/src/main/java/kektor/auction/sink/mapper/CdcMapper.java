package kektor.auction.sink.mapper;

import kektor.auction.sink.dto.msg.*;
import kektor.auction.sink.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.ProblemDetail;

import java.util.List;

@Mapper(config = MapConfig.class)
public interface CdcMapper {

    Bid toModel(BidCDC cdc);

    BalanceOperation toModel(BalanceOperationCDC cdc);

    PaymentAccount toModel(PaymentAccountCDC cdc);

    Saga toModel(SagaCDC cdc);

    Lot toModel(LotCDC cdc);

    @Mapping(source = "categoryCDC", target = ".")
    @Mapping(source = "hierarchyIds", target = "categoryHierarchyIds")
    Category toModel(CategoryCDC cdc, List<Long> hierarchyIds);

    default String convertToString(ProblemDetail detail) {
        return detail.toString();
    }
}
