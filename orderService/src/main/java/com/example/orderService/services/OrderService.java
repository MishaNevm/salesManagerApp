package com.example.orderService.services;

import com.example.orderService.models.Order;
import com.example.orderService.repositoryes.OrderRepository;
import com.example.orderService.util.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(int id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public void save(Order order) {
        order.setCreatedAt(new Date());
        orderRepository.save(order);
    }

    public void update(Order order) {
        order.setUpdatedAt(new Date());
        orderRepository.save(order);
    }

    public void delete(int id) {
        orderRepository.deleteById(id);
    }

//    public void addProductToOrder(Order order, int productId, int quantity) {
//        if (quantity == 0) {
//            throw new ProductNotAddException("Количество товара указано 0, для удаления воспользуйтесь другой командой");
//        }
//        if (productId == 0) {
//            throw new ProductNotFoundException();
//        }
//        ProductOrder productOrder = new ProductOrder();
//        productOrder.setOrder(order);
//        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
//        if (product.getQuantity() < quantity) {
//            throw new ProductNotAddException("Количество добавляемого товара в счет меньше наличия товара");
//        } else product.setQuantity(product.getQuantity() - quantity);
//        productOrder.setProduct(product);
//        productOrder.setQuantity(quantity);
//        productOrder.setCreatedAt(new Date());
//        productOrderRepository.save(productOrder);
//    }


//    public void setQuantityProductInOrder(int orderId, int productId, int quantity) {
//        if (quantity == 0) {
//            throw new ProductNotAddException("Количество товара указано 0, для удаления воспользуйтесь другой командой");
//        }
//        if (productId == 0) {
//            throw new ProductNotFoundException();
//        }
//        ProductOrder productOrder = productOrderRepository.findByOrderId(orderId)
//                .stream().filter(a -> a.getProduct().getId() == productId).findAny().orElseThrow(ProductNotFoundException::new);
//        Product product = productOrder.getProduct();
//        if (product.getQuantity() + productOrder.getQuantity() < quantity) {
//            throw new ProductNotAddException("Количество добавляемого товара меньше общего наличия");
//        }
//        product.setQuantity(product.getQuantity() + productOrder.getQuantity() - quantity);
//        productOrder.setQuantity(quantity);
//        productOrder.setUpdatedAt(new Date());
//        productOrderRepository.save(productOrder);
//    }

//    public void deleteProductFromOrder(int orderId, int productId) {
//        ProductOrder productOrder = productOrderRepository.findByOrderId(orderId)
//                .stream().filter(a -> a.getProduct().getId() == productId).findFirst().orElseThrow(ProductNotFoundException::new);
//        Product product = productOrder.getProduct();
//        product.setQuantity(product.getQuantity() + productOrder.getQuantity());
//        productOrderRepository.deleteById(productOrder.getId());
//    }
//
//    public void deleteAllProductsFromOrder(int orderId) {
//        for (ProductOrder productOrder : productOrderRepository.findByOrderId(orderId)) {
//            Product product = productOrder.getProduct();
//            product.setQuantity(product.getQuantity() + productOrder.getQuantity());
//            productOrderRepository.deleteById(productOrder.getId());
//        }
//    }
}
