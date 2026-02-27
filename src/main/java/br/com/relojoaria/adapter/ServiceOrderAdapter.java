package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.request.ServiceOrderRequest;
import br.com.relojoaria.dto.response.ServiceOrderResponse;
import br.com.relojoaria.entity.Client;
import br.com.relojoaria.entity.ServiceOrder;
import br.com.relojoaria.entity.SubService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {MaterialUsageAdapter.class, SubServiceAdapter.class})
public interface ServiceOrderAdapter {

    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "addValue", source = "addValue")
    @Mapping(target = "subServicesPrice", expression = "java(calculateSubServicesPrice(entity))")
    ServiceOrderResponse toResponseDTO(ServiceOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", expression = "java(client)")
    ServiceOrder toEntity(ServiceOrderRequest dto, @Context Client client);

    @Mapping(target = "clientName", source="client.name")
    ServiceOrderRequest toRequest(ServiceOrder entity);

    default BigDecimal calculateSubServicesPrice(ServiceOrder entity) {
        if (entity.getSubServices() == null) return BigDecimal.ZERO;
        return entity.getSubServices().stream()
                .map(SubService::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
