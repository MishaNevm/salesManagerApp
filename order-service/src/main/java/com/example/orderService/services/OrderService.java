package com.example.orderService.services;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.dto.OrderDTOResponse;
import com.example.orderService.models.Order;
import com.example.orderService.repositoryes.OrderRepository;
import com.example.orderService.util.ModelMapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapperUtil modelMapperUtil;

    public OrderService(OrderRepository orderRepository, ModelMapperUtil modelMapperUtil) {
        this.orderRepository = orderRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    @Transactional(readOnly = true)
    public OrderDTOResponse findAll() {
        List<OrderDTO> orderDTOs;
        orderDTOs = orderRepository.findAll().stream()
                .map(modelMapperUtil::convertOrderToOrderDTO)
                .collect(Collectors.toList());
        return createOrderDTOResponse(orderDTOs);
    }

    @Transactional(readOnly = true)
    public OrderDTOResponse findById(int id) {
        Order order = orderRepository.findById(id).orElse(new Order());
        OrderDTO orderDTO = modelMapperUtil.convertOrderToOrderDTO(order);
        return createOrderDTOResponse(Collections.singletonList(orderDTO));
    }

    @Transactional(readOnly = true)
    public OrderDTOResponse findByClientShortName(String clientShortName) {
        List<OrderDTO> orderDTOs = orderRepository.findByClientShortName(clientShortName).stream()
                .map(modelMapperUtil::convertOrderToOrderDTO)
                .collect(Collectors.toList());
        return createOrderDTOResponse(orderDTOs);
    }

    public void save(OrderDTO orderDTO) {
        Order order = modelMapperUtil.convertOrderDTOToOrder(orderDTO);
        order.setCreatedAt(new Date());
        orderRepository.save(order);
    }

    public void update(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId()).orElse(new Order());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(new Date());
        orderRepository.save(modelMapperUtil.convertOrderDTOToOrder(orderDTO));
    }

    public void delete(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return;
        }
        orderRepository.delete(order);
    }

    private OrderDTOResponse createOrderDTOResponse(List<OrderDTO> orderDTOs) {
        OrderDTOResponse orderDTOResponse = new OrderDTOResponse();
        orderDTOResponse.setResponse(orderDTOs);
        return orderDTOResponse;
    }
}

