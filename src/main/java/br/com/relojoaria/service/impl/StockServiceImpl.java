package br.com.relojoaria.service.impl;

import br.com.relojoaria.adapter.StockAdapter;
import br.com.relojoaria.dto.request.StockRequest;
import br.com.relojoaria.dto.response.StockResponse;
import br.com.relojoaria.entity.Product;
import br.com.relojoaria.entity.Stock;
import br.com.relojoaria.error.exception.NotFoundException;
import br.com.relojoaria.repository.ProductRepository;
import br.com.relojoaria.repository.StockRepository;
import br.com.relojoaria.service.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockAdapter stockAdapter;
    private final ProductRepository productRepository;

    @Override
    public StockResponse create(StockRequest dto) {
        Product product = productRepository.findByName(dto.getProductName())
                .orElseThrow(() -> new NotFoundException("produto não encontrado"));
        Optional<Stock> existentStock = stockRepository.findByProductName(dto.getProductName());

        if(existentStock.isPresent()) {
            throw new RuntimeException("ja existe um stock cadastrado com este produto");
        }
        Stock stock = Stock.builder()
                .product(product)
                .currentQuantity(dto.getQuantity())
                .build();

        stockRepository.save(stock);
        return stockAdapter.toResponse(stock);
    }

    @Override
    public StockResponse updateStock(Long id, BigDecimal quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow(
                () -> new NotFoundException("nenhum estoque encontrado")
        );
        BigDecimal currentQuantity = stock.getCurrentQuantity();
        if(quantity.compareTo(BigDecimal.ZERO) < 0 && currentQuantity.compareTo(quantity.abs()) > 0) {
            stock.setCurrentQuantity(stock.getCurrentQuantity().subtract(quantity));
        }else
            stock.setCurrentQuantity(stock.getCurrentQuantity().add(quantity));

        // Relacionar inserir os campos e relações das novas entidades
        stockRepository.save(stock);
        return stockAdapter.toResponse(stock);
    }

    @Override
    public void delete(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(
                () -> new NotFoundException("estoque com id: "+id+" não encontrado")
        );
        stockRepository.delete(stock);
    }

    @Override
    public StockResponse getById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(
                () -> new NotFoundException("estoque com id: "+id+" não encontrado")
        );
        return stockAdapter.toResponse(stock);
    }

    @Override
    public List<StockResponse> getAll() {
        List<StockResponse> stocks = stockRepository.findAll().stream()
                .map(stockAdapter::toResponse).collect(Collectors.toList());
        if (stocks.isEmpty()) {
            return new ArrayList<>();
        }
        return stocks;
    }
}
