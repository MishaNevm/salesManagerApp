package org.example.orderService.mappers;

import org.example.orderService.OrderServiceOuterClass;
import org.example.orderService.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    public void testConvertOrderToOuterOrder() {
        Order order = new Order();
        order.setId(1);
        order.setClientShortName("Client A");
        order.setComment("Test order");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setCreatedBy("User A");
        order.setUpdatedBy("User B");

        OrderServiceOuterClass.Order outerOrder = orderMapper.convertOrderToOuterOrder(order);

        assertEquals(order.getId(), outerOrder.getId());
        assertEquals(order.getClientShortName(), outerOrder.getClientShortName());
        assertEquals(order.getComment(), outerOrder.getComment());
        assertEquals(order.getCreatedAt().getTime(), orderMapper.parseDateString(outerOrder.getCreatedAt()).getTime(), 1000);
        assertEquals(order.getUpdatedAt().getTime(), orderMapper.parseDateString(outerOrder.getUpdatedAt()).getTime(), 1000);
        assertEquals(order.getCreatedBy(), outerOrder.getCreatedBy());
        assertEquals(order.getUpdatedBy(), outerOrder.getUpdatedBy());

    }

    @Test
    public void testConvertOuterOrderToOrder() {
        Date testDate = new Date();
        OrderServiceOuterClass.Order outerOrder = OrderServiceOuterClass.Order.newBuilder()
                .setId(1)
                .setClientShortName("Client A")
                .setComment("Test order")
                .setCreatedAt(testDate.toString())
                .setUpdatedAt(testDate.toString())
                .setCreatedBy("User A")
                .setUpdatedBy("User B")
                .build();

        Order order = orderMapper.convertOuterOrderToOrder(outerOrder);

        assertEquals(outerOrder.getId(), order.getId());
        assertEquals(outerOrder.getClientShortName(), order.getClientShortName());
        assertEquals(outerOrder.getComment(), order.getComment());
        assertEquals(testDate.getTime(),orderMapper.parseDateString(outerOrder.getCreatedAt()).getTime(), 1000);
        assertEquals(testDate.getTime(),orderMapper.parseDateString(outerOrder.getUpdatedAt()).getTime(), 1000);
        assertEquals(outerOrder.getCreatedBy(), order.getCreatedBy());
        assertEquals(outerOrder.getUpdatedBy(), order.getUpdatedBy());
    }
}