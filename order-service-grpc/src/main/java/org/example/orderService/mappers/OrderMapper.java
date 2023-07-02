package org.example.orderService.mappers;

import org.example.orderService.OrderServiceOuterClass;
import org.example.orderService.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderMapper {


    public OrderServiceOuterClass.Order convertOrderToOuterOrder(Order order) {
        OrderServiceOuterClass.Order.Builder builder = OrderServiceOuterClass.Order.newBuilder();
        if (order.getId() != 0) {
            builder.setId(order.getId());
        }
        if (order.getComment() != null) {
            builder.setComment(order.getComment());
        }
        if (order.getClientShortName() != null) {
            builder.setClientShortName(order.getClientShortName());
        }
        if (order.getCreatedAt() != null) {
            builder.setCreatedAt(order.getCreatedAt().toString());
        }
        if (order.getUpdatedAt() != null) {
            builder.setUpdatedAt(order.getUpdatedAt().toString());
        }
        if (order.getCreatedBy() != null) {
            builder.setCreatedBy(order.getCreatedBy());
        }
        if (order.getUpdatedBy() != null) {
            builder.setUpdatedBy(order.getUpdatedBy());
        }
        return builder.build();
    }

    public Order convertOuterOrderToOrder(OrderServiceOuterClass.Order orderToConvert) {
        Order order = new Order();

        order.setId(orderToConvert.getId());
        order.setClientShortName(orderToConvert.getClientShortName());
        order.setComment(orderToConvert.getComment());

        String createdAtString = orderToConvert.getCreatedAt();
        Date createdAt = parseDateString(createdAtString);
        order.setCreatedAt(createdAt);

        String updatedAtString = orderToConvert.getUpdatedAt();
        Date updatedAt = parseDateString(updatedAtString);
        order.setUpdatedAt(updatedAt);


        order.setCreatedBy(orderToConvert.getCreatedBy());
        order.setUpdatedBy(orderToConvert.getUpdatedBy());

        return order;
    }

    public Date parseDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
