package br.com.relojoaria.adapter;

import br.com.relojoaria.dto.request.SubServiceRequest;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.entity.ServiceOrder;
import br.com.relojoaria.entity.SubService;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class SubServiceAdapter {

    public  SubServiceResponse toResponse(SubService subServiceOrder) {
        return null;
    }

    public SubService toEntity(SubServiceRequest sub, ServiceOrder serviceOrder) {
        if(sub.getTitle() == null || sub.getTitle().isBlank()){
            return null;
        }
        SubService subService = new SubService();
        subService.setTitle(sub.getTitle());
        subService.setDescription(sub.getDescription());
        subService.setPrice(sub.getPrice() != null ? sub.getPrice() : BigDecimal.ZERO);

        subService.setServiceOrder(serviceOrder);

        return subService;
    }

    public List<SubService> toEntityList(List<SubServiceRequest> subServices, ServiceOrder serviceOrder) {
        return subServices.stream()
                .map(s -> toEntity(s,serviceOrder))
                .filter(Objects::nonNull)
                .toList();
    }
}
