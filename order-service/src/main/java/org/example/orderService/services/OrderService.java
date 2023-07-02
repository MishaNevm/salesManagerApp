package org.example.orderService.services;

import io.grpc.stub.StreamObserver;
import org.example.orderService.OrderServiceGrpc;
import org.example.orderService.OrderServiceOuterClass;
import org.example.orderService.dao.OrderDAO;
import org.example.orderService.mappers.OrderMapper;
import org.example.orderService.models.Order;

import java.util.List;


public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderDAO orderDao;
    private final OrderMapper orderMapper;


    public OrderService() {
        this.orderDao = OrderDAO.getInstance();
        this.orderMapper = new OrderMapper();
    }

    @Override
    public void getAllOrders(com.google.protobuf.Empty request, StreamObserver<org.example.orderService.OrderServiceOuterClass.OrderResponse> responseObserver) {
        List<Order> orderList = orderDao.findAll();
        OrderServiceOuterClass.OrderResponse orderResponse = OrderServiceOuterClass.OrderResponse.newBuilder()
                .addAllOrders(orderList.stream().map(orderMapper::convertOrderToOuterOrder).toList()).build();
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getById(OrderServiceOuterClass.Id request,
                        StreamObserver<org.example.orderService.OrderServiceOuterClass.Order> responseObserver) {
        Order order = orderDao.findById(request.getId());
        responseObserver.onNext(orderMapper.convertOrderToOuterOrder(order));
        responseObserver.onCompleted();
    }

    @Override
    public void save(OrderServiceOuterClass.Order request,
                     StreamObserver<org.example.orderService.OrderServiceOuterClass.Result> responseObserver) {
        boolean result = orderDao.save(orderMapper.convertOuterOrderToOrder(request));
        responseObserver.onNext(OrderServiceOuterClass.Result.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
    }


    @Override
    public void update(OrderServiceOuterClass.Order request,
                       StreamObserver<org.example.orderService.OrderServiceOuterClass.Result> responseObserver) {
        boolean result = orderDao.update(orderMapper.convertOuterOrderToOrder(request));
        responseObserver.onNext(OrderServiceOuterClass.Result.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(OrderServiceOuterClass.Id request,
                       StreamObserver<org.example.orderService.OrderServiceOuterClass.Result> responseObserver) {
        boolean result = orderDao.delete(request.getId());
        responseObserver.onNext(OrderServiceOuterClass.Result.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
    }
}
