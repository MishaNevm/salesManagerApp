package com.example.inventoryService.repositoryes;

import com.example.inventoryService.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByProductId(int productId);
    List<ProductOrder> findByOrderId(int orderId);
    Optional<ProductOrder> findByOrderIdAndProductId(int orderId, int productId);

    @Modifying
    @Query("DELETE FROM ProductOrder po WHERE po.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") int orderId);
}
