package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ModelMapperUtil;
import com.example.inventoryService.util.ProductNotAddException;
import com.example.inventoryService.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ModelMapperUtil modelMapperUtil;

    public ProductService(ProductRepository productRepository, ProductOrderRepository productOrderRepository, ModelMapperUtil modelMapperUtil) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findAll() {
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(productRepository.findAll().stream().map(modelMapperUtil::convertProductToProductDTO).toList());
        return productDTOResponse;
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findById(int id) {
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(Collections.singletonList(modelMapperUtil.convertProductToProductDTO(productRepository.findById(id).orElseThrow(ProductNotFoundException::new))));
        return productDTOResponse;
    }

    public void save(ProductDTO productDTO) {
        Product product = modelMapperUtil.convertProductDTOToProduct(productDTO);
        Optional<Product> productFromDb = productRepository.findByName(product.getName());
        if (productFromDb.isPresent()) {
            Product product2 = productFromDb.get();
            product2.setUpdatedAt(new Date());
            product2.setQuantity(productFromDb.get().getQuantity() + product.getQuantity());
        } else {
            product.setCreatedAt(new Date());
            productRepository.save(product);
        }
    }

    public void update(ProductDTO productDTO) {
        Product product = modelMapperUtil.convertProductDTOToProduct(productDTO);
        product.setUpdatedAt(new Date());
        productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    public void addProductToOrder(Product product, Integer orderId, Integer quantity) {
        if (quantity == null) {
            throw new ProductNotAddException("Количество товара указано 0, для удаления воспользуйтесь другой командой");
        }
        if (orderId == null) {
            throw new ProductNotFoundException();
        }
        if (product.getQuantity() < quantity) {
            throw new ProductNotAddException("Количество добавляемого товара в счет меньше наличия товара");
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(product);
        productOrder.setQuantity(quantity);
        product.setQuantity(product.getQuantity() - quantity);
        productOrder.setOrderId(orderId);
        productOrder.setCreatedAt(new Date());
        productOrderRepository.save(productOrder);
    }

//    public void changeOrderInProduct(int productId, Integer oldOrderId, Integer newOrderId) {
//        if (oldOrderId == null || newOrderId == null) {
//            throw new OrderNotFoundException();
//        }
//        ProductOrder productOrder = productOrderRepository.findByProductId(productId)
//                .stream().filter(a -> a.getOrder().getId() == oldOrderId)
//                .findFirst().orElseThrow(OrderNotFoundException::new);
//        productOrder.setOrder(orderRepository.findById(newOrderId).orElseThrow(OrderNotFoundException::new));
//        productOrder.setCreatedAt(new Date());
//        productOrderRepository.save(productOrder);
//    }
}
