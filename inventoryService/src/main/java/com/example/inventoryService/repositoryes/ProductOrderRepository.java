package com.example.inventoryService.repositoryes;

import com.example.inventoryService.models.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByProductId(int productId);
    List<ProductOrder> findByOrderId(int orderId);
}
