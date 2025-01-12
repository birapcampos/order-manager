package br.com.campos.order.repository;

import br.com.campos.order.repository.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String> {

    List<OrderEntity> findAll();
    List<OrderEntity> findByOrderDateAndStatus(LocalDate orderDate, String status);
    List<OrderEntity> findByOrderDate(LocalDate orderDate);
    List<OrderEntity> findByStatus(String status);
}
