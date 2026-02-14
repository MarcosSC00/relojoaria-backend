package br.com.relojoaria.service.impl;

import br.com.relojoaria.adapter.ServiceOrderAdapter;
import br.com.relojoaria.adapter.SubServiceAdapter;
import br.com.relojoaria.dto.request.MaterialUsageRequest;
import br.com.relojoaria.dto.request.ServiceOrderRequest;
import br.com.relojoaria.dto.request.ServiceOrderUpdate;
import br.com.relojoaria.dto.request.SubServiceRequest;
import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.dto.response.ServiceOrderResponse;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.entity.*;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.ClientRepository;
import br.com.relojoaria.repository.ServiceOrderRepository;
import br.com.relojoaria.repository.StockRepository;
import br.com.relojoaria.repository.SubServiceRepository;
import br.com.relojoaria.service.ServiceOrderService;
import br.com.relojoaria.service.SubServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceOrderImpl implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderAdapter serviceOrderAdapter;
    private final ClientRepository clientRepository;
    private final StockRepository stockRepository;
    private final SubServiceService subServiceOrderService;
    private final SubServiceRepository subRepository;
    private final SubServiceAdapter subServiceAdapter;


    @Override
    public List<ServiceOrderResponse> getAll() {
        return serviceOrderRepository.findAllOrderedById().stream()
                .map(serviceOrderAdapter::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceOrderResponse getById(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servço com id:"+id+" não encontrado"));
        return serviceOrderAdapter.toResponseDTO(serviceOrder);
    }

    @Override
    public ServiceOrderResponse create(ServiceOrderRequest request) {
        // Validar cliente
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Requester com ID: " + request.getClientId()+" não encontrado"));

        // Criar oredem de serviço base
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .client(client)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .type(request.getType())
                .addValue(request.getAddValue() != null ? request.getAddValue() : BigDecimal.ZERO)
                .endDate(request.getEndDate())
                .totalPrice(BigDecimal.ZERO)
                .subServices(new ArrayList<>())
                .items(new ArrayList<>())
                .build();

        // Processar itens do estoque
        processStockItems(serviceOrder, request.getItems());

        // Calcular preço total
        calculateTotalPrice(serviceOrder);

        ServiceOrder savedServiceOrder = serviceOrderRepository.save(serviceOrder);

        return serviceOrderAdapter.toResponseDTO(savedServiceOrder);
    }

    @Override
    public ServiceOrderResponse update(Long serviceOrderId, ServiceOrderUpdate dto) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Serviço com id:"+serviceOrderId+" não encontrado"));
        Client client = clientRepository
                .findById(dto.getRequesterId() != null ? dto.getRequesterId() : serviceOrder.getClient().getId())
                .orElseThrow(() -> new NotFoundException(" requester não encontrado"));

        serviceOrder.setClient(client);
        serviceOrder.setTitle(dto.getTitle() != null ? dto.getTitle() : serviceOrder.getTitle());
        serviceOrder.setDescription(dto.getDescription() != null ? dto.getDescription() : serviceOrder.getDescription());
        serviceOrder.setStatus(dto.getStatus() != null ? dto.getStatus() : serviceOrder.getStatus());
        serviceOrder.setEndDate(dto.getEndDate() != null ? dto.getEndDate() : serviceOrder.getEndDate());
        serviceOrder.setAddValue(dto.getAddValue() != null ? dto.getAddValue() : serviceOrder.getAddValue());

        //novo valor total
        calculateTotalPrice(serviceOrder);

        serviceOrderRepository.save(serviceOrder);
        return serviceOrderAdapter.toResponseDTO(serviceOrder);

    }

    @Override
    public void delete(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("task com id:"+id+" não encontrada"));
        serviceOrderRepository.delete(serviceOrder);
    }

    @Override
    public SubServiceResponse addSubServiceOrder(Long serviceOrderId, SubServiceRequest dto) {
        return subServiceOrderService.create(serviceOrderId, dto);
    }

    @Override
    public void removeSubServiceOrder(Long serviceOrderId, Long subServiceId) {
        SubService sub = subRepository.findById(subServiceId)
                .orElseThrow(() -> new NotFoundException("sub-serviço não encontrado"));
        subServiceOrderService.removeSubService(serviceOrderId, sub);
    }

    @Override
    public List<SubServiceResponse> getSubServiceOrders(Long serviceOrderId) {
        List<SubService> subServices = serviceOrderRepository.getSubServiceOrders(serviceOrderId);
        if (subServices.isEmpty()) {
            throw new NotFoundException("Nenhum sub-serviço encontrado");
        }
        return  subServices.stream().map(subServiceAdapter::toResponse).toList();
    }

    @Override
    public List<ServiceOrderCustom> getServiceOrderCustoms(String productName) {
        List<ServiceOrderCustom> servicesCustom = serviceOrderRepository.getServiceOrderCustom(productName);
        if (servicesCustom.isEmpty()) {
            return new ArrayList<>();
        }
        return servicesCustom;
    }

    private void processStockItems(ServiceOrder serviceOrder, List<MaterialUsageRequest> stockItems) {

        if (stockItems == null || stockItems.isEmpty()) return;
        for (MaterialUsageRequest itemRequest : stockItems) {
            // Buscar stock pelo nome do item
            Stock stock = stockRepository.findByProductName(itemRequest.getProductName())
                    .orElseThrow(() -> new NotFoundException(
                            "Estoque não possui o item: " + itemRequest.getProductName()));

            // Validar quantidade disponível
            validateStockQuantity(stock, itemRequest.getQuantityUsed());

            // Calcular subtotal
            BigDecimal subTotal = stock.getProduct().getPrice().multiply(itemRequest.getQuantityUsed());

            // Criar MaterialUsage
            MaterialUsage materialUsage = MaterialUsage.builder()
                    .serviceOrder(serviceOrder)
                    .product(stock.getProduct())
                    .quantityUsed(itemRequest.getQuantityUsed())
                    .subTotal(subTotal)
                    .build();

            serviceOrder.getItems().add(materialUsage);

            // Atualizar estoque
            updateStockQuantity(stock, itemRequest.getQuantityUsed());
        }
    }

    private void validateStockQuantity(Stock stock, BigDecimal qtdUsed) {
        if (stock.getCurrentQuantity().compareTo(qtdUsed) < 0) {
            throw new NotFoundException( "Quantidade insuficiente em estoque");
        }
    }

    private void updateStockQuantity(Stock stock, BigDecimal qtdUsed) {
        BigDecimal newQuantity = stock.getCurrentQuantity().subtract(qtdUsed);
        stock.setCurrentQuantity(newQuantity);
        stock.setQtdUsed(stock.getQtdUsed().add(qtdUsed));
        stockRepository.save(stock);
    }

    private void calculateTotalPrice(ServiceOrder serviceOrder) {
        BigDecimal serviceOrderValue = serviceOrder.getItems().stream()
                .map(MaterialUsage::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal subServiceOrderValue = serviceOrder.getSubServices().stream()
                .map(SubService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal materialsPrice = serviceOrderValue.add(subServiceOrderValue);

        BigDecimal addValue = serviceOrder.getAddValue() != null ? serviceOrder.getAddValue() : BigDecimal.ZERO;
        serviceOrder.setTotalPrice(materialsPrice.add(addValue));
    }
}
