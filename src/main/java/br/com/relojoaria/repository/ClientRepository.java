package br.com.relojoaria.repository;

import br.com.relojoaria.dto.ClientCustomDto;
import br.com.relojoaria.entity.Client;
import br.com.relojoaria.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByName(String name);

    @Query(value = """
    SELECT\s
        c.id,\s
        c.name,\s
        c.phone,\s
        c.created_at AS createdAt
    FROM client c
    WHERE c.id = :id
   \s""", nativeQuery = true)
    Optional<ClientCustomDto> findClientWithoutOrders(@Param("id") Long id);

    @Query(value = """
            SELECT\s
            c.id,\s
            c.name,\s
            c.phone,\s
            c.created_at AS createdAt
            FROM client c
            ORDER BY c.id ASC
            """, nativeQuery = true)
    List<ClientCustomDto> findAllOrderedById();

    Optional<Client> findByName(String name);

    @Query("SELECT so FROM ServiceOrder so where so.client.id = :id")
    List<ServiceOrder> findServiceOrdersById(@Param("id") Long id);

    @Query(value = """
            select c.name from client c""", nativeQuery = true)
    List<String> getAllClientNames();
}
