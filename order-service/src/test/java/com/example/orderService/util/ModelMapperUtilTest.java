package com.example.orderService.util;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.models.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class ModelMapperUtilTest {

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Test
    void convertOrderDTOToOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(2);
        orderDTO.setClientShortName("Test");
        orderDTO.setCreatedAt(new Date());

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setClientShortName(orderDTO.getClientShortName());
        order.setCreatedAt(orderDTO.getCreatedAt());

        Assertions.assertEquals(order, modelMapperUtil.convertOrderDTOToOrder(orderDTO));
    }

    @Test
    void convertOrderToOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(2);
        orderDTO.setClientShortName("Test");
        orderDTO.setCreatedAt(new Date());

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setClientShortName(orderDTO.getClientShortName());
        order.setCreatedAt(orderDTO.getCreatedAt());

        Assertions.assertEquals(orderDTO, modelMapperUtil.convertOrderToOrderDTO(order));
    }
}