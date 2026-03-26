package br.com.relojoaria.repository;

import br.com.relojoaria.dto.response.ServiceOrderCustom;
import br.com.relojoaria.dto.response.SubServiceResponse;
import br.com.relojoaria.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    @Query("SELECT s FROM ServiceOrder s ORDER BY s.id ASC")
    List<ServiceOrder> findAllOrderedById();

    @Query(value = """
            select ss.title,
            ss.description,
            ss.price from sub_service ss
            inner join service_order so
            on ss.service_order_id=so.id
            where so.id=:serviceId""", nativeQuery = true)
    List<SubServiceResponse> getSubServiceOrders(@Param("serviceId") Long serviceId);

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

    @Query(value = """
    select extract(month from s.created_at), sum(s.total_price) as month from service_order s
    where extract(year from s.created_at) = '2026'
    group by extract(month from s.created_at)
    order by extract(month from s.created_at);
    """, nativeQuery = true)
    List<Object[]> countServicesByMonth(@Param("year") int year);
}
