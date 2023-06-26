package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ErrorResponse;
import com.example.inventoryService.util.ModelMapperUtil;
import com.example.inventoryService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        if (productOrders.isEmpty()) {
            return productDTOResponse;
        }

        List<ProductDTO> productDTOs = productOrders.stream()
                .map(this::mapProductOrderToDTO)
                .collect(Collectors.toList());

        productDTOResponse.setResponse(productDTOs);
        return productDTOResponse;
    }

    public void save(ProductOrderDTO productOrderDTO, ErrorResponse errorResponse) {
        ValidationError validationError = new ValidationError();
        Optional<ProductOrder> productOrderOptional = productOrderRepository.findByOrderIdAndProductId(productOrderDTO.getOrderId(), productOrderDTO.getProduct().getId());
        if (productOrderOptional.isPresent()) {
            ProductOrder productOrder = productOrderOptional.get();
            Product product = productOrder.getProduct();
            checkQuantity(product.getQuantity(), productOrderDTO.getQuantity(), validationError);
            if (validationError.getField() == null) {
                product.setQuantity(product.getQuantity() - productOrderDTO.getQuantity());
                productOrder.setQuantity(productOrder.getQuantity() + productOrderDTO.getQuantity());
            }
        } else {
            productRepository.findById(productOrderDTO.getProduct().getId()).ifPresent(product -> {
                checkQuantity(product.getQuantity(), productOrderDTO.getQuantity(), validationError);
                if (validationError.getField() == null) {
                    product.setQuantity(product.getQuantity() - productOrderDTO.getQuantity());
                    productOrderRepository.save(modelMapperUtil.convertProductOrderDTOToProductOrder(productOrderDTO));
                }
            });
        }
        setErrorResponse(errorResponse, validationError);
    }

    public void updateProductQuantityInOrder(ProductOrderDTO productOrderDTO, ErrorResponse errorResponse) {
        ValidationError validationError = new ValidationError();
        productOrderRepository.findByOrderIdAndProductId(productOrderDTO.getOrderId(), productOrderDTO.getProduct().getId()).ifPresent(productOrder -> {
            Product product = productOrder.getProduct();
            checkQuantity(product.getQuantity() + productOrder.getQuantity(), productOrderDTO.getQuantity(), validationError);
            if (validationError.getField() == null) {
                product.setQuantity(product.getQuantity() + productOrder.getQuantity() - productOrderDTO.getQuantity());
                productOrder.setQuantity(productOrderDTO.getQuantity());
            }
        });
        setErrorResponse(errorResponse, validationError);
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
        productOrderRepository.findByOrderIdAndProductId(orderId, productId).ifPresent(productOrder -> {
            Product product = productOrder.getProduct();
            product.setQuantity(product.getQuantity() + productOrder.getQuantity());
            productOrderRepository.deleteById(productOrder.getId());
        });
    }

    private void checkQuantity(int quantityInProduct, int quantityInProductOrder, ValidationError validationError) {
        if (quantityInProduct < quantityInProductOrder) {
            validationError.setField("quantity");
            validationError.setCode("0");
            validationError.setMessage("Количество добавляемого товара меньше товара в наличии");
        }
    }

    private ProductDTO mapProductOrderToDTO(ProductOrder productOrder) {
        ProductDTO productDTO = modelMapperUtil.convertProductToProductDTO(productOrder.getProduct());
        productDTO.setQuantity(productOrder.getQuantity());
        return productDTO;
    }

    private void setErrorResponse(ErrorResponse errorResponse, ValidationError validationError) {
        if (validationError.getField() != null) {
            errorResponse.setErrors(Collections.singletonList(validationError));
        } else {
            errorResponse.setErrors(new ArrayList<>());
        }
    }
}

