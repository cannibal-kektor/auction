package kektor.auction.payment.mapper;

import org.mapstruct.*;
import org.mapstruct.control.NoComplexMapping;

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        typeConversionPolicy = ReportingPolicy.ERROR,
        mappingControl = NoComplexMapping.class,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface MapConfig {
}

