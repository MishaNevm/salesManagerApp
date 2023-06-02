package com.example.Frontend.controllers;


import com.example.Frontend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;
    private final String GET_ALL_ORDERS = "http://localhost:8484/orders";
    private final String GET_ALL_PRODUCTS = "http://localhost:8484/products";
    private final String GET_ORDER_BY_ID = "http://localhost:8484/orders/%d";
    private final String CREATE_ORDER = GET_ALL_ORDERS;
    private final String ADD_PRODUCT_TO_ORDER = "http://localhost:8484/orders/%d/add-product?product-id=%d&quantity=%d";
    private final String UPDATE_ORDER = GET_ORDER_BY_ID;
    private final String DELETE_ORDER = GET_ORDER_BY_ID;

    @Autowired
    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("orders", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_ORDERS, OrderDTOResponse.class)).getResponse());
        return "order/getAllOrders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", restTemplate.getForObject(String.format(GET_ORDER_BY_ID,id), OrderDTO.class));
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderDTO> entity = new HttpEntity<>(orderDTO, headers);
        restTemplate.exchange(CREATE_ORDER, HttpMethod.POST, entity, HttpStatus.class);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        model.addAttribute("order", restTemplate.getForObject(GET_ORDER_BY_ID + id, OrderDTO.class));
        return "order/updateOrder";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("order") @Valid OrderDTO orderDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "order/updateOrder";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDTO> entity = new HttpEntity<>(orderDTO, headers);
        restTemplate.exchange(UPDATE_ORDER + id, HttpMethod.PATCH, entity, HttpStatus.class);
        return "redirect:/orders/" + id;
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange(DELETE_ORDER + id, HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable("id") int id ,Model model) {
        model.addAttribute("id", id);
        model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_PRODUCTS, ProductDTOResponse.class)).getResponse());
        return "order/addProductToOrder";
    }


    @PostMapping("/{id}/add-product")
    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
                                                        @RequestParam(value = "product-id", required = false) Integer productId,
                                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
        restTemplate.exchange(String.format(ADD_PRODUCT_TO_ORDER, id, productId, quantity), HttpMethod.POST, null, HttpStatus.class);
        return ResponseEntity.ok(HttpStatus.OK);
    }
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
