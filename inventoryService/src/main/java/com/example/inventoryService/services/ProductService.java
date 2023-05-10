package com.example.inventoryService.services;

import com.example.inventoryService.models.Product;
import com.example.inventoryService.models.ProductOrder;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ProductNotAddException;
import com.example.inventoryService.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;

    public ProductService(ProductRepository productRepository, ProductOrderRepository productOrderRepository) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(int id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public void save(Product product) {
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

    public void update(Product product) {
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
