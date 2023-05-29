package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.OrderDTO;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final Producer producer;
    private final Consumer consumer;

    public OrderController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @GetMapping
    public String findAll(Model model) throws InterruptedException {
        producer.sendRequestToOrderService(MethodsCodes.GET_ALL_ORDERS, new OrderDTO());
        model.addAttribute("orders", consumer.getResponseMap().get(MethodsCodes.GET_ALL_ORDERS).take().getResponse());
        return "order/getAllOrders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.GET_ORDER_BY_ID, orderDTO);
        model.addAttribute("order", consumer.getResponseMap().get(MethodsCodes.GET_ORDER_BY_ID).take().getResponse().get(0));
        return "order/getOrderById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("order") OrderDTO orderDTO) {
        return "order/createOrder";
    }

    @PostMapping
    public String create(@ModelAttribute("order") @Valid OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "order/createOrder";
        }
        producer.sendRequestToOrderService(MethodsCodes.CREATE_ORDER, orderDTO);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String update (@PathVariable("id") int id, Model model) throws InterruptedException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.GET_ORDER_BY_ID, orderDTO);
        model.addAttribute("order", consumer.getResponseMap().get(MethodsCodes.GET_ORDER_BY_ID).take().getResponse().get(0));
        return "order/updateOrder";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("order") @Valid OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "order/updateOrder";
        }
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.UPDATE_ORDER, orderDTO);
        return "redirect:/orders/" + id;
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.DELETE_ORDER, orderDTO);
        return "redirect:/orders";
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


//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

//    private Order checkRequest(Integer clientId, OrderDTO orderDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new OrderNotCreatedException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
//        Order order = modelMapperUtil.convertOrderDTOToOrder(orderDTO);
//        if (clientId != null) {
//            order.setClientId(clientId);
//        }
//        return order;
//    }
}
