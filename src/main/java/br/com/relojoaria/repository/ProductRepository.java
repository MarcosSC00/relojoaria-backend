package br.com.relojoaria.repository;

import br.com.relojoaria.dto.response.ProductAnalysis;
import br.com.relojoaria.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);

    @Query(value = """
            select
                p.id,
                p.name,
                p.unit as product_unit,
                max(s.current_qtd) as current_qtd,
                max(p.price) as price,
                sum(s.qtd_used) as qtd_used,
                sum(p.price * s.qtd_used) as total_value
                from product p
                inner join stock s on s.product_id=p.id where p.name=:productName
                group by p.id, p.name, p.unit""", nativeQuery = true)
    ProductAnalysis getProductAnalysisByName(@Param("productName") String productName);
}
