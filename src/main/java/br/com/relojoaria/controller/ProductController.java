package br.com.relojoaria.controller;

import br.com.relojoaria.dto.ProductDto;
import br.com.relojoaria.dto.response.ProductAnalysis;
import br.com.relojoaria.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Validated
@RequiredArgsConstructor
public class ProductController {

    public final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        return new ResponseEntity<>(productService.create(dto),  HttpStatus.CREATED);
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductDto> getProductByName(@PathVariable("productName") String productName) {
        return ResponseEntity.ok(productService.getByName(productName));
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductDto> update(@PathVariable("productName") String productName,
                                                    @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.update(productName, dto));
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<Void> delete(@PathVariable("productName") String productName) {
        productService.delete(productName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analysis/{productName}")
    public ResponseEntity<ProductAnalysis> getProductAnalysis(@PathVariable("productName") String productName) {
        productService.getProductAnalysis(productName);
        return ResponseEntity.ok(productService.getProductAnalysis(productName));
    }

    @GetMapping("/get-just-name")
    public ResponseEntity<List<String>> getJustNameProducts() {
        return ResponseEntity.ok(productService.getJustNameProducts());
    }
}
