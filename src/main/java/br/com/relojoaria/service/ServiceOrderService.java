package br.com.relojoaria.service;

import br.com.relojoaria.dto.request.ServiceOrderRequest;
import br.com.relojoaria.dto.request.SubServiceRequest;
import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.dto.response.ServiceOrderResponse;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.enums.ServiceStatus;

import java.util.List;

public interface ServiceOrderService {
    List<ServiceOrderResponse> getAll();
    ServiceOrderResponse getById(Long id);
    ServiceOrderResponse create(ServiceOrderRequest dto);
    ServiceOrderResponse update(Long id, ServiceOrderRequest dto);
    void delete(Long id);
    SubServiceResponse addSubServiceOrder(Long serviceOrderId, SubServiceRequest dto);
    void removeSubServiceOrder(Long serviceOrderId, Long subServiceId);
    List<SubServiceResponse> getSubServiceOrders(Long serviceOrderId);
    List<ServiceOrderCustom> getServiceOrderCustoms(String productName);
    void updateStatus(Long id, ServiceStatus status);

}
