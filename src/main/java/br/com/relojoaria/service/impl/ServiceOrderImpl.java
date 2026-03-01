package br.com.relojoaria.service.impl;

import br.com.relojoaria.adapter.MaterialUsageAdapter;
import br.com.relojoaria.adapter.ServiceOrderAdapter;
import br.com.relojoaria.adapter.SubServiceAdapter;
import br.com.relojoaria.dto.request.*;
import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.dto.response.ServiceOrderResponse;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.entity.*;
import br.com.relojoaria.enums.ServiceStatus;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.ClientRepository;
import br.com.relojoaria.repository.ServiceOrderRepository;
import br.com.relojoaria.repository.StockRepository;
import br.com.relojoaria.repository.SubServiceRepository;
import br.com.relojoaria.service.ServiceOrderService;
import br.com.relojoaria.service.StockService;
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
    private final StockService stockService;
    private final MaterialUsageAdapter  materialUsageAdapter;


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
        Client client = clientRepository.findByName(request.getClientName())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        // Criar oredem de serviço base
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .client(client)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .type(request.getType())
                .addValue(request.getAddValue() != null ? request.getAddValue() : BigDecimal.ZERO)
                .items(new ArrayList<>())
                .endDate(request.getEndDate())
                .totalPrice(BigDecimal.ZERO)
                .subServices(new ArrayList<>())
                .build();

        //Criacao do subservico diretamente
        if(request.getSubServices() != null && !request.getSubServices().isEmpty()) {
            for(SubServiceRequest subRequest : request.getSubServices()) {
                SubService subService = SubService.builder()
                        .serviceOrder(serviceOrder)
                        .title(subRequest.getTitle())
                        .description(subRequest.getDescription())
                        .price(subRequest.getPrice())
                        .build();

                serviceOrder.getSubServices().add(subService);
            }
        }
        // Processar itens do estoque
        processStockItems(serviceOrder, request.getItems());

        // Calcular preço total
        calculateTotalPrice(serviceOrder);

        ServiceOrder savedServiceOrder = serviceOrderRepository.save(serviceOrder);

        return serviceOrderAdapter.toResponseDTO(savedServiceOrder);
    }

    @Override
    public ServiceOrderResponse update(Long serviceOrderId, ServiceOrderRequest dto) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Serviço com id:"+serviceOrderId+" não encontrado"));

        if(dto.getClientName() != null){
            Client client = clientRepository
                    .findByName(dto.getClientName())
                    .orElseThrow(() -> new NotFoundException(" requester não encontrado"));
            serviceOrder.setClient(client);
        }

        serviceOrder.setTitle(dto.getTitle() != null ? dto.getTitle() : serviceOrder.getTitle());
        serviceOrder.setDescription(dto.getDescription() != null ? dto.getDescription() : serviceOrder.getDescription());
        serviceOrder.setType(dto.getType() != null ? dto.getType() : serviceOrder.getType());
        serviceOrder.setStatus(dto.getStatus() != null ? dto.getStatus() : serviceOrder.getStatus());
        serviceOrder.setEndDate(dto.getEndDate() != null ? dto.getEndDate() : serviceOrder.getEndDate());
        serviceOrder.setAddValue(dto.getAddValue() != null ? dto.getAddValue() : serviceOrder.getAddValue());
        if (dto.getItems() != null) {
            serviceOrder.getItems().clear();
            List<MaterialUsage> items =
                    materialUsageAdapter.toEntityList(dto.getItems());
            items.forEach(item -> item.setServiceOrder(serviceOrder));
            serviceOrder.getItems().addAll(items);
        }
        if (dto.getSubServices() != null) {
            serviceOrder.getSubServices().clear();
            List<SubService> subservices = subServiceAdapter
                    .toEntityList(dto.getSubServices(), serviceOrder);
            serviceOrder.getSubServices().addAll(subservices);
        }

        //novo valor total
        calculateTotalPrice(serviceOrder);

        serviceOrderRepository.save(serviceOrder);
        return serviceOrderAdapter.toResponseDTO(serviceOrder);

    }

    @Override
    public void delete(Long id) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("task com id:"+id+" não encontrada"));
        serviceOrder.getItems().forEach((m) ->
                stockService.updateQtdUsed(m.getProduct().getStock().getId(), m.getQuantityUsed()));
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

    @Override
    public void updateStatus(Long id, ServiceStatus status) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço com id:"+id+" não encontrado"));
        serviceOrder.setStatus(status != null ?  status : serviceOrder.getStatus());
        serviceOrderRepository.save(serviceOrder);
    }

    private void processStockItems(ServiceOrder serviceOrder, List<MaterialUsageRequest> stockItems) {

        if (stockItems == null || stockItems.isEmpty()) return;
        for (MaterialUsageRequest itemRequest : stockItems) {
            if(itemRequest.getProductName().isBlank() || itemRequest.getQuantityUsed() == null){
                break;
            }
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
