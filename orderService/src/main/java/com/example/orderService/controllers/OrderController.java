package com.example.orderService.controllers;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.models.Order;
import com.example.orderService.services.OrderService;
import com.example.orderService.util.ErrorResponse;
import com.example.orderService.util.ModelMapperUtil;
import com.example.orderService.util.OrderNotCreatedException;
import com.example.orderService.util.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapperUtil modelMapperUtil;

    public OrderController(OrderService orderService, ModelMapperUtil modelMapperUtil) {
        this.orderService = orderService;
        this.modelMapperUtil = modelMapperUtil;
    }

    @GetMapping
    public List<OrderDTO> findAll() {
        return orderService.findAll().stream().map(modelMapperUtil::convertOrderToOrderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") int id) {
        return modelMapperUtil.convertOrderToOrderDTO(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid OrderDTO orderDTO,
                                             @RequestParam(value = "client-id", required = false) Integer clientId,
                                             BindingResult bindingResult) {
        orderService.save(checkRequest(clientId, orderDTO, bindingResult));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestParam(value = "client-id", required = false) Integer clientId,
                                             @RequestBody @Valid OrderDTO orderDTO,
                                             BindingResult bindingResult) {
        orderDTO.setCreatedAt(orderService.findById(id).getCreatedAt());
        orderDTO.setId(id);
        orderService.update(checkRequest(clientId, orderDTO, bindingResult));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        orderService.findById(id);
        orderService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @PostMapping("/{id}/add-product")
//    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
//                                                        @RequestParam("product-id") Integer productId,
//                                                        @RequestParam("quantity") Integer quantity) {
//        orderService.addProductToOrder(orderService.findById(id), productId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @PatchMapping("/{id}/set-product-quantity")
//    public ResponseEntity<HttpStatus> setProductQuantity(@PathVariable("id") int id,
//                                                         @RequestParam(value = "product-id", required = false) Integer productId,
//                                                         @RequestParam(value = "quantity", required = false) Integer quantity) {
//        orderService.setQuantityProductInOrder(id, productId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}/deleteProduct")
//    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id,
//                                                    @RequestParam(value = "product-id", required = false) Integer productId) {
//        orderService.findById(id);
//        if (productId == null) {
//            orderService.deleteAllProductsFromOrder(id);
//        } else {
//            orderService.deleteProductFromOrder(id, productId);
//        }
//        return ResponseEntity.ok(HttpStatus.OK);
//    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private Order checkRequest(Integer clientId, OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new OrderNotCreatedException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        Order order = modelMapperUtil.convertOrderDTOToOrder(orderDTO);
        if (clientId != null) {
            order.setClientId(clientId);
        }
        return order;
    }
}
