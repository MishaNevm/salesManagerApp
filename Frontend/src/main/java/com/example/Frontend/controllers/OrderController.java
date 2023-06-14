package com.example.Frontend.controllers;


import com.example.Frontend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;
    protected static final String GET_ALL_ORDERS = "http://localhost:8484/orders";
    protected static final String GET_ALL_PRODUCTS = "http://localhost:8484/products";
    protected static final String GET_ORDER_BY_ID = "http://localhost:8484/orders/%d";
    protected static final String GET_PRODUCTS_BY_ORDER_ID = "http://localhost:8484/products/get-products-by-order?order-id=%d";
    protected static final String CREATE_ORDER = "http://localhost:8484/orders?client-id=%d";
    protected static final String ADD_PRODUCT_TO_ORDER = "http://localhost:8484/orders/%d/add-product?product-id=%d&quantity=%d";
    protected static final String UPDATE_ORDER = "http://localhost:8484/orders/%d?client-id=%d";
    protected static final String DELETE_ORDER = GET_ORDER_BY_ID;
    protected static final String DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID = "http://localhost:8484/orders/%d/delete-products";
    protected static final String DELETE_PRODUCT_IN_ORDER_BY_ORDER_ID_AND_PRODUCT_ID = "http://localhost:8484/orders/%d/delete-from-order?product-id=%d";
    protected static final String UPDATE_PRODUCT_QUANTITY_IN_ORDER = "http://localhost:8484/orders/%d/update-product-quantity?product-id=%d&quantity=%d";
    protected static final String GET_ORDERS_BY_CLIENT_ID = "http://localhost:8484/orders?client-id=%d";

    private List<ProductDTO> productDTOList;


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
        model.addAttribute("order", restTemplate.getForObject(String.format(GET_ORDER_BY_ID, id), OrderDTO.class));
        productDTOList = Objects.requireNonNull(restTemplate
                .getForObject(String
                        .format(GET_PRODUCTS_BY_ORDER_ID, id), ProductDTOResponse.class)).getResponse();
        model.addAttribute("products", productDTOList);
        return "order/getOrderById";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(ClientController.GET_ALL_CLIENTS, ClientDTOResponse.class))
                .getResponse());
        return "order/createOrder";
    }

    @PostMapping
    public String create(@RequestParam("client-id") int clientId) {
        restTemplate.exchange(String.format(CREATE_ORDER, clientId), HttpMethod.POST, null, HttpStatus.class);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int orderId, Model model) throws InterruptedException {
        model.addAttribute("id", orderId);
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(ClientController.GET_ALL_CLIENTS, ClientDTOResponse.class))
                .getResponse());
        return "order/updateOrder";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int orderId, @RequestParam(value = "client-id", required = false) int clientId) {
        restTemplate.exchange(String.format(UPDATE_ORDER, orderId, clientId), HttpMethod.PATCH, null, HttpStatus.class);
        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(Model model, @PathVariable("id") int orderId) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("products", productDTOList);
        return "order/updateProductInOrder";
    }

    @PatchMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(@PathVariable("id") int orderId,
                                               @RequestParam(value = "product-id", required = false) Integer productId,
                                               @RequestParam(value = "quantity", required = false) Integer quantity) {
        restTemplate.exchange(String.format(UPDATE_PRODUCT_QUANTITY_IN_ORDER, orderId, productId, quantity), HttpMethod.PATCH, null, HttpStatus.class);
        productDTOList = null;
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}/delete-from-order")
    public String deleteProductInOrderByOrderIdAndProductId(@PathVariable("id") int orderId, @RequestParam(value = "product-id", required = false) Integer productId) {
        restTemplate.exchange(String.format(DELETE_PRODUCT_IN_ORDER_BY_ORDER_ID_AND_PRODUCT_ID, orderId, productId), HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}/delete-products")
    public String deleteAllProductsInOrder(@PathVariable("id") int orderId) {
        restTemplate.exchange(String.format(DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID, orderId), HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange(String.format(DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID, id), HttpMethod.DELETE, null, HttpStatus.class);
        restTemplate.exchange(String.format(DELETE_ORDER, id), HttpMethod.DELETE, null, HttpStatus.class);

        return "redirect:/orders";
    }

    @GetMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_PRODUCTS, ProductDTOResponse.class)).getResponse());
        return "order/addProductToOrder";
    }


    @PostMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable("id") int id,
                                    @RequestParam(value = "product-id", required = false) Integer productId,
                                    @RequestParam(value = "quantity", required = false) Integer quantity) {
        restTemplate.exchange(String.format(ADD_PRODUCT_TO_ORDER, id, productId, quantity), HttpMethod.POST, null, HttpStatus.class);
        return "redirect:/orders/" + id;
    }
}
