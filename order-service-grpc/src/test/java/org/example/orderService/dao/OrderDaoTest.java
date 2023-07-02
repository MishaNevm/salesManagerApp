package org.example.orderService.dao;

import org.example.orderService.models.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        // Создаем новый заказ
        Order order = new Order();
        order.setCreatedAt(new Date());

        // Вызываем метод save
        boolean result = orderDao.save(order);

        Assertions.assertTrue(result);

        // Проверяем, что заказ был сохранен и имеет идентификатор
        Assertions.assertNotNull(order.getId());
    }

    @Test
    public void testFindAll() {

        List<Order> ordersFromDB = orderDao.findAll();

        Assertions.assertNotNull(ordersFromDB);
    }

    @Test
    public void testFindById() {
        // Задаем идентификатор существующего заказа

        Order order = new Order();
        order.setComment("Test");

        orderDao.save(order);
        // Вызываем метод findById
        Order orderFromDb = orderDao.findById(order.getId());


        Assertions.assertNotNull(orderFromDb);

        // Проверяем, что идентификатор заказа соответствует заданному значению
        Assertions.assertEquals(order, orderFromDb);
    }


    @Test
    public void testUpdate() {
        // Задаем идентификатор существующего заказа

        // Вызываем метод findById для получения существующего заказа
        Order order = new Order();
        order.setComment("Test");

        orderDao.save(order);
        // Проверяем, что заказ не равен null
        System.out.println(order.getId());

        // Обновляем данные заказа

        // Вызываем метод update
        boolean result = orderDao.update(order);

        // Проверяем, что метод успешно обновляет заказ
        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete() {
        // Задаем идентификатор существующего заказа
        int orderId = 1;

        // Вызываем метод delete
        boolean result = orderDao.delete(orderId);

        // Проверяем, что метод успешно удаляет заказ
        Assertions.assertTrue(result);
    }
}
