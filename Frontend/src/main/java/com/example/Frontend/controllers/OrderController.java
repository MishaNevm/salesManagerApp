package com.example.Frontend.controllers;


import com.example.Frontend.dto.*;
import com.example.Frontend.util.CurrentUser;
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
    private final CurrentUser currentUser;
    protected static final String GET_ALL_ORDERS = "http://localhost:8484/orders";
    protected static final String GET_ALL_PRODUCTS = "http://localhost:8484/products";
    protected static final String GET_ORDER_BY_ID = "http://localhost:8484/orders/%d";
    protected static final String GET_PRODUCTS_BY_ORDER_ID = "http://localhost:8484/products/get-products-by-order?order-id=%d";
    protected static final String CREATE_ORDER = GET_ALL_ORDERS;
    protected static final String ADD_PRODUCT_TO_ORDER = "http://localhost:8484/orders/%d/add-product?product-id=%d&quantity=%d";
    protected static final String UPDATE_ORDER = GET_ORDER_BY_ID;
    protected static final String DELETE_ORDER = GET_ORDER_BY_ID;
    protected static final String DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID = "http://localhost:8484/orders/%d/delete-products";
    protected static final String DELETE_PRODUCT_IN_ORDER_BY_ORDER_ID_AND_PRODUCT_ID = "http://localhost:8484/orders/%d/delete-from-order?product-id=%d";
    protected static final String UPDATE_PRODUCT_QUANTITY_IN_ORDER = "http://localhost:8484/orders/%d/update-product-quantity?product-id=%d&quantity=%d";
    protected static final String GET_ORDERS_BY_CLIENT_ID = "http://localhost:8484/orders?client-id=%d";


    @Autowired
    public OrderController(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("orders", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_ORDERS, OrderDTOResponse.class)).getResponse());
        return "order/getAllOrders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", restTemplate.getForObject(String.format(GET_ORDER_BY_ID, id), OrderDTO.class));
        model.addAttribute("products", Objects.requireNonNull(restTemplate
                .getForObject(String
                        .format(GET_PRODUCTS_BY_ORDER_ID, id), ProductDTOResponse.class)).getResponse());
        return "order/getOrderById";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(ClientController.GET_ALL_CLIENTS, ClientDTOResponse.class))
                .getResponse());
        model.addAttribute("order", new OrderDTO());
        return "order/createOrder";
    }

    @PostMapping
    public String create(@ModelAttribute("order") OrderDTO orderDTO) {
        orderDTO.setCreatedBy(currentUser.getEmail());
        restTemplate.postForObject(CREATE_ORDER, orderDTO, HttpStatus.class);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        model.addAttribute("order", restTemplate.getForObject(String.format(GET_ORDER_BY_ID, id), OrderDTO.class));
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(ClientController.GET_ALL_CLIENTS, ClientDTOResponse.class))
                .getResponse());
        return "order/updateOrder";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("order") OrderDTO orderDTO) {
        orderDTO.setUpdatedBy(currentUser.getEmail());
        restTemplate.patchForObject(UPDATE_ORDER, orderDTO, HttpStatus.class);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("products", Objects.requireNonNull(restTemplate
                .getForObject(String
                        .format(GET_PRODUCTS_BY_ORDER_ID, id), ProductDTOResponse.class)).getResponse());
        return "order/updateProductInOrder";
    }

    @PatchMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(@PathVariable("id") int id,
                                               @RequestParam(value = "quantity", required = false) Integer quantity,
                                               @RequestParam(value = "product-id", required = false) Integer productId) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(id);
        productOrderDTO.setUpdatedBy(currentUser.getEmail());
        if (productId != null) {
            productOrderDTO.setProduct(new ProductDTO());
            productOrderDTO.getProduct().setId(productId);
        }
        if (quantity != null) {
            productOrderDTO.setQuantity(quantity);
            restTemplate.patchForObject(UPDATE_PRODUCT_QUANTITY_IN_ORDER, productOrderDTO, HttpStatus.class);
        } else return "order/updateProductInOrder";
        return "redirect:/orders/" + id;
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
