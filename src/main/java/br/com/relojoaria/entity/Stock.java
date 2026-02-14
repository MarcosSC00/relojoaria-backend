package br.com.relojoaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "current_qtd",  precision = 10, scale = 2)
    private BigDecimal currentQuantity;

    @Column(name = "min_qtd",   precision = 10, scale = 2)
    private BigDecimal minQuantity;

    @Column(name = "qtd_used")
    private BigDecimal qtdUsed;
}
