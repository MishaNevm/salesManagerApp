package com.example.orderService.services;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.dto.OrderDTOResponse;
import com.example.orderService.models.Order;
import com.example.orderService.repositoryes.OrderRepository;
import com.example.orderService.util.ModelMapperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ModelMapperUtil modelMapperUtil;

    @Test
    void findAllTest() {
        Order order = new Order();
        order.setClientShortName("Test");
        Mockito.doReturn(Collections.singletonList(order)).when(orderRepository).findAll();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setClientShortName(order.getClientShortName());
        Mockito.doReturn(orderDTO).when(modelMapperUtil).convertOrderToOrderDTO(order);

        OrderDTOResponse orderDTOResponse = orderService.findAll();
        Assertions.assertNotNull(orderDTOResponse);
        assertEquals("Test", orderDTOResponse.getResponse().get(0).getClientShortName());
    }

    @Test()
    void findAllNullTest() {
        OrderDTOResponse orderDTOResponse = orderService.findAll();
        Assertions.assertTrue(orderDTOResponse.getResponse().isEmpty());
    }

    @Test
    void findByIdTest() {
        Order order = new Order();
        order.setId(2);
        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(2);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(2);
        Mockito.doReturn(orderDTO).when(modelMapperUtil).convertOrderToOrderDTO(order);
        assertEquals(orderDTO, orderService.findById(2).getResponse().get(0));
    }

    @Test()
    void findByIdFailTest() {
        Mockito.doReturn(new OrderDTO()).when(modelMapperUtil).convertOrderToOrderDTO(new Order());
        assertEquals(new OrderDTO(), orderService.findById(0).getResponse().get(0));
    }

    @Test
    void findByClientShortNameTest() {
        Order order = new Order();
        order.setClientShortName("Test");
        Mockito.doReturn(Collections.singletonList(order)).when(orderRepository).findByClientShortName(order.getClientShortName());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setClientShortName(order.getClientShortName());
        Mockito.doReturn(orderDTO).when(modelMapperUtil).convertOrderToOrderDTO(order);
        List<OrderDTO> orderDTOList = Collections.singletonList(orderDTO);
        assertEquals(orderDTOList, orderService.findByClientShortName(order.getClientShortName()).getResponse());
    }

    @Test
    void findByClientShortNameNullTest() {
        Assertions.assertTrue(orderService.findByClientShortName("Test").getResponse().isEmpty());
    }

    @Test
    void saveTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setClientShortName("Test");
        Order order = new Order();
        order.setClientShortName("Test");
        Mockito.doReturn(order).when(modelMapperUtil).convertOrderDTOToOrder(orderDTO);
        orderService.save(orderDTO);
        Mockito.verify(modelMapperUtil, Mockito.times(1)).convertOrderDTOToOrder(orderDTO);
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
        Assertions.assertEquals(new Date().getTime(), order.getCreatedAt().getTime(), 1000);
    }

    @Test
    void update() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(10);
        Date date = new Date();
        Order order = new Order();
        order.setId(10);
        order.setCreatedAt(date);
        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(orderDTO.getId());
        orderService.update(orderDTO);
        Mockito.verify(modelMapperUtil, Mockito.times(1)).convertOrderDTOToOrder(orderDTO);
        Assertions.assertEquals(date, orderDTO.getCreatedAt());
        Assertions.assertEquals(date.getTime(), orderDTO.getUpdatedAt().getTime(), 1000);
    }

    @Test
    void updateFailTest() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(10);
        Mockito.doReturn(Optional.empty()).when(orderRepository).findById(orderDTO.getId());
        Order order = new Order();
        order.setId(orderDTO.getId());
        Mockito.doReturn(order).when(modelMapperUtil).convertOrderDTOToOrder(orderDTO);
        orderService.update(orderDTO);
        Mockito.verify(modelMapperUtil, Mockito.times(1)).convertOrderDTOToOrder(orderDTO);
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
        Assertions.assertEquals(new Date().getTime(), order.getCreatedAt().getTime(), 1000);
    }

    @Test
    void deleteTest() {
        Order order = new Order();
        order.setId(10);

        Mockito.doReturn(Optional.of(order)).when(orderRepository).findById(order.getId());

        orderService.delete(order.getId());

        Mockito.verify(orderRepository, Mockito.times(1)).findById(order.getId());
        Mockito.verify(orderRepository, Mockito.times(1)).delete(order);
    }

    @Test
    void deleteFailTest() {

        Order order = new Order();
        order.setId(10);

        Mockito.doReturn(Optional.empty()).when(orderRepository).findById(order.getId());

        orderService.delete(order.getId());

        Mockito.verify(orderRepository, Mockito.times(0)).delete(order);
    }
}