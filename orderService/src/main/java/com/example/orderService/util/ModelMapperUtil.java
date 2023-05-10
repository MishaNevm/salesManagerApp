package com.example.orderService.util;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {


    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Order convertOrderDTOToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public OrderDTO convertOrderToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
