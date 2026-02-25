package br.com.relojoaria.service;

import br.com.relojoaria.dto.request.StockRequest;
import br.com.relojoaria.dto.response.StockResponse;

import java.math.BigDecimal;
import java.util.List;

public interface StockService {
    StockResponse create(StockRequest dto);
    StockResponse updateStock(StockRequest dto);
    void delete(Long id);
    StockResponse getById(Long id);
    List<StockResponse> getAll();
    void updateQtdUsed(Long serviceId, BigDecimal qtdUsed);
}
