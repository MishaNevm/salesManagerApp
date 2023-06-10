package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ModelMapperUtil modelMapperUtil;

    private final ProductRepository productRepository;

    @Autowired
    public ProductOrderService(ProductOrderRepository productOrderRepository, ModelMapperUtil modelMapperUtil, ProductRepository productRepository) {
        this.productOrderRepository = productOrderRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findByOrderId(int orderId) {
        List<ProductOrder> productOrders = productOrderRepository.findByOrderId(orderId);
        if (productOrders.isEmpty()) return new ProductDTOResponse();
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(new ArrayList<>());
        for (ProductOrder productOrder : productOrders) {
            ProductDTO productDTO = modelMapperUtil.convertProductToProductDTO(productOrder.getProduct());
            productDTO.setQuantity(productOrder.getQuantity());
            productDTOResponse.getResponse().add(productDTO);
        }
        return productDTOResponse;
    }

    public void save(ProductOrderDTO productOrderDTO) {
        Optional<ProductOrder> productOrderOptional = productOrderRepository.findByOrderIdAndProductId(productOrderDTO.getOrderId(), productOrderDTO.getProduct().getId());
        if (productOrderOptional.isPresent()) {
            ProductOrder productOrder = productOrderOptional.get();
            Product product = productOrder.getProduct();
            product.setQuantity(product.getQuantity() - productOrderDTO.getQuantity());
            productOrder.setQuantity(productOrder.getQuantity() + productOrderDTO.getQuantity());
        } else {
            productRepository.findById(productOrderDTO.getProduct().getId()).ifPresent(a -> a.setQuantity(a.getQuantity() - productOrderDTO.getQuantity()));
            productOrderRepository.save(modelMapperUtil.convertProductOrderDTOToProductOrder(productOrderDTO));
        }
    }


    public void updateProductQuantityInOrder(ProductOrderDTO productOrderDTO) {
        productOrderRepository.findByOrderIdAndProductId(productOrderDTO.getOrderId(), productOrderDTO.getProduct().getId()).ifPresent(a -> {
            Product product = a.getProduct();
            product.setQuantity(product.getQuantity() + a.getQuantity() - productOrderDTO.getQuantity());
            a.setQuantity(productOrderDTO.getQuantity());
        });
    }

    public void deleteAllProductsByOrderId(int orderId) {
        List<ProductOrder> productOrders = productOrderRepository.findByOrderId(orderId);
        if (!productOrders.isEmpty()) {
            for (ProductOrder productOrder : productOrders) {
                Product product = productOrder.getProduct();
                product.setQuantity(product.getQuantity() + productOrder.getQuantity());
            }
            productOrderRepository.deleteByOrderId(orderId);
        }
    }

    public void deleteByOrderIdAndProductId(int orderId, int productId) {
        productOrderRepository.findByOrderIdAndProductId(orderId, productId).ifPresent(a -> {
            Product product = a.getProduct();
            product.setQuantity(product.getQuantity() + a.getQuantity());
            productOrderRepository.deleteById(a.getId());
        });
    }
}
