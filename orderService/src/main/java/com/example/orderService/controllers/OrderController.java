package com.example.orderService.controllers;

import com.example.salesManagerApp.dao.ClientDAO;
import com.example.salesManagerApp.dto.OrderDTO;
import com.example.salesManagerApp.models.Order;
import com.example.salesManagerApp.services.OrderService;
import com.example.salesManagerApp.util.ErrorResponse;
import com.example.salesManagerApp.util.ModelMapperUtil;
import com.example.salesManagerApp.util.orderUtil.OrderNotCreatedException;
import com.example.salesManagerApp.util.orderUtil.OrderNotFoundException;
import com.example.salesManagerApp.util.productUtil.ProductNotAddException;
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

    private final ClientDAO clientDAO;

    public OrderController(OrderService orderService, ModelMapperUtil modelMapperUtil, ClientDAO clientDAO) {
        this.orderService = orderService;
        this.modelMapperUtil = modelMapperUtil;
        this.clientDAO = clientDAO;
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

    @PostMapping("/{id}/add-product")
    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
                                                        @RequestParam("product-id") Integer productId,
                                                        @RequestParam("quantity") Integer quantity) {
        orderService.addProductToOrder(orderService.findById(id), productId, quantity);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/set-product-quantity")
    public ResponseEntity<HttpStatus> setProductQuantity(@PathVariable("id") int id,
                                                         @RequestParam(value = "product-id", required = false) Integer productId,
                                                         @RequestParam(value = "quantity", required = false) Integer quantity) {
        orderService.setQuantityProductInOrder(id, productId, quantity);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/deleteProduct")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id,
                                                    @RequestParam(value = "product-id", required = false) Integer productId) {
        orderService.findById(id);
        if (productId == null) {
            orderService.deleteAllProductsFromOrder(id);
        } else {
            orderService.deleteProductFromOrder(id, productId);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotAddException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private Order checkRequest(Integer clientId, OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new OrderNotCreatedException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        Order order = modelMapperUtil.convertOrderDTOToOrder(orderDTO);
        if (clientId != null) {
            order.setClient(clientDAO.loadClientById(clientId));
        }
        return order;
    }
}
