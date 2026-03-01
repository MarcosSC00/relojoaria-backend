package br.com.relojoaria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "material_usage")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @Column(name="qtd_used", precision = 10, scale = 2)
    private BigDecimal quantityUsed;

    @Column(name = "sub_total",  precision = 10, scale = 2)
    private BigDecimal subTotal;
}
