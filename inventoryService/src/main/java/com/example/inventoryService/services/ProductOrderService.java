package com.example.inventoryService.services;

import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    @Autowired
    public ProductOrderService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    public List<ProductOrder> findByOrderId(int orderId) {
        return productOrderRepository.findByOrderId(orderId);
    }
}
