package br.com.relojoaria.controller;

import br.com.relojoaria.dto.request.ServiceOrderRequest;
import br.com.relojoaria.dto.request.ServiceOrderUpdate;
import br.com.relojoaria.dto.request.SubServiceRequest;
import br.com.relojoaria.dto.response.ServiceOrderResponse;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.service.ServiceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-order")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;

    @GetMapping
    public ResponseEntity<List<ServiceOrderResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> create(@RequestBody @Valid ServiceOrderRequest request) {
        ServiceOrderResponse order = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponse> update(@PathVariable Long id,
                                                       @RequestBody @Valid ServiceOrderUpdate dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PostMapping("/{serviceOrderId}/add-subservice")
    public ResponseEntity<SubServiceResponse> addSubService(@PathVariable("serviceOrderId") Long serviceOrderId,
                                                            @RequestBody @Valid SubServiceRequest dto) {
        return ResponseEntity.ok(service.addSubServiceOrder(serviceOrderId, dto));
    }

    @PostMapping("{serviceOrderId}/remove-subservice/{subServiceId}")
    public ResponseEntity<Void> removeSubService(@PathVariable("serviceOrderId") Long serviceOrderId,
                                                 @PathVariable("subServiceId") Long subServiceId) {
        service.removeSubServiceOrder(serviceOrderId, subServiceId);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("{serviceOrderId}/sub-services")
    public ResponseEntity<List<SubServiceResponse>> getSubServices(@PathVariable("serviceOrderId")
                                                                       Long serviceOrderId) {
        return ResponseEntity.ok(service.getSubServiceOrders(serviceOrderId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
