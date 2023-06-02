package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ModelMapperUtil modelMapperUtil;

    @Autowired
    public ProductOrderService(ProductOrderRepository productOrderRepository, ModelMapperUtil modelMapperUtil) {
        this.productOrderRepository = productOrderRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    public List<ProductOrder> findByOrderId(int orderId) {
        return productOrderRepository.findByOrderId(orderId);
    }

    @Transactional
    public void save(ProductOrderDTO productOrderDTO) {
        productOrderRepository.save(modelMapperUtil.convertProductOrderDTOToProductOrder(productOrderDTO));
    }
}
