package org.example.orderService.dao;

import org.example.orderService.models.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class OrderDaoTest {

    private OrderDAO orderDao;

    @BeforeEach
    public void setup() {
        orderDao = OrderDAO.getInstance();
    }

    @Test
    public void testSave() {
        Order order = new Order();
        order.setCreatedAt(new Date());


        boolean result = orderDao.save(order);

        Assertions.assertTrue(result);


        Assertions.assertTrue(order.getId() != 0);
    }

    @Test
    public void testFindAll() {
        Order order = new Order();
        order.setCreatedAt(new Date());

        orderDao.save(order);

        List<Order> ordersFromDB = orderDao.findAll();

        Assertions.assertFalse(ordersFromDB.isEmpty());
    }

    @Test
    public void testFindById() {

        Order order = new Order();
        order.setComment("Test");

        orderDao.save(order);

        Order orderFromDb = orderDao.findById(order.getId());


        Assertions.assertNotNull(orderFromDb);

        Assertions.assertEquals(order, orderFromDb);
    }


    @Test
    public void testUpdate() {

        Order order = new Order();
        order.setComment("Test");

        orderDao.save(order);

        System.out.println(order.getId());


        boolean result = orderDao.update(order);


        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete() {

        int orderId = 1;

        boolean result = orderDao.delete(orderId);

        Assertions.assertTrue(result);
    }
}
