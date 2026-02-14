package br.com.relojoaria.repository;

import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.entity.ServiceOrder;
import br.com.relojoaria.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    @Query("SELECT s FROM ServiceOrder s ORDER BY s.id ASC")
    List<ServiceOrder> findAllOrderedById();

    @Query("SELECT ss FROM SubService ss where ss.serviceOrder.id = :serviceId")
    List<SubService> getSubServiceOrders(@Param("serviceId") Long serviceId);

    @Query(value = """
            select
                s.id,
                s.title,
                sum(m.qtd_used) as qtd_used,
                sum(m.sub_total) as sub_service,
                count(s.id) as amount_service
            	from service_order s inner join material_usage m
            	on s.id=m.service_order_id inner join product p on p.id=m.product where p.name=:productName
            	group by s.id, s.title""", nativeQuery = true)
    List<ServiceOrderCustom> getServiceOrderCustom(@Param("productName") String productName);
}
