package com.example.orderService.controllers;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.dto.OrderDTOResponse;
import com.example.orderService.kafka.Producer;
import com.example.orderService.services.OrderService;
import com.example.orderService.util.MethodsCodes;
import com.example.orderService.util.OrderNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class OrderController {

    private final OrderService orderService;
    private final Producer producer;

    public OrderController(OrderService orderService, Producer producer) {
        this.orderService = orderService;
        this.producer = producer;
    }


    public void findAll() {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ALL_ORDERS.getCode(), orderService.findAll());
    }

    public void findByClientShortName(String clientShortName) {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME.getCode(), orderService.findByClientShortName(clientShortName));
    }

    public void findById(int id) {
        try {
            producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDER_BY_ID.getCode(), orderService.findById(id));
        } catch (OrderNotFoundException e) {
            producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDER_BY_ID.getCode(), new OrderDTOResponse());
        }
    }

    public void create(OrderDTO orderDTO) {
        orderService.save(orderDTO);
    }

    public void update(OrderDTO orderDTO) {
        orderService.update(orderDTO);
    }


    public void delete(int id) {
        orderService.delete(id);
    }
}
