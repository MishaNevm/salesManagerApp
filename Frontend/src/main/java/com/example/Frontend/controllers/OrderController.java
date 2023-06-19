package com.example.Frontend.controllers;


import com.example.Frontend.dto.ClientDTOResponse;
import com.example.Frontend.dto.OrderDTO;
import com.example.Frontend.dto.OrderDTOResponse;
import com.example.Frontend.dto.ProductDTOResponse;
import com.example.Frontend.util.CurrentUser;
import com.example.Frontend.util.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;
    private final CurrentUser currentUser;


    @Autowired
    public OrderController(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("orders",
                Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_ORDERS.getUrl(), OrderDTOResponse.class))
                        .getResponse());
        return "order/getAllOrders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", restTemplate.getForObject(String.format(Urls.GET_ORDER_BY_ID.getUrl(), id), OrderDTO.class));
        model.addAttribute("products", Objects.requireNonNull(restTemplate
                .getForObject(String
                        .format(Urls.GET_PRODUCTS_BY_ORDER_ID.getUrl(), id), ProductDTOResponse.class)).getResponse());
        return "order/getOrderById";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(Urls.GET_ALL_CLIENTS.getUrl(), ClientDTOResponse.class))
                .getResponse());
        model.addAttribute("order", new OrderDTO());
        return "order/createOrder";
    }

    @PostMapping
    public String create(@ModelAttribute("order") OrderDTO orderDTO, @RequestParam(value = "client-short-name", required = false) String clientShortName) {
        orderDTO.setClientShortName(clientShortName);
        orderDTO.setCreatedBy(currentUser.getEmail());
        restTemplate.postForObject(Urls.CREATE_ORDER.getUrl(), orderDTO, HttpStatus.class);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String updateClient(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", restTemplate.getForObject(String.format(Urls.GET_ORDER_BY_ID.getUrl(), id), OrderDTO.class));
        model.addAttribute("clients", Objects
                .requireNonNull(restTemplate
                        .getForObject(Urls.GET_ALL_CLIENTS.getUrl(), ClientDTOResponse.class))
                .getResponse());
        return "order/updateClientInOrder";
    }

    @PatchMapping("/{id}")
    public String updateClient(@PathVariable("id") int id,
                         @ModelAttribute("order") OrderDTO orderDTO,
                         @RequestParam(value = "client-short-name", required = false) String clientShortName,
                         @RequestParam(value = "created-by", required = false) String createdBy,
                         @RequestParam(value = "comment", required = false) String comment) {
        orderDTO.setUpdatedBy(createdBy);
        orderDTO.setComment(comment);
        orderDTO.setClientShortName(clientShortName);
        orderDTO.setUpdatedBy(currentUser.getEmail());
        restTemplate.patchForObject(String.format(Urls.UPDATE_ORDER.getUrl(), id), orderDTO, HttpStatus.class);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/{id}/update-comment")
    public String updateComment(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", restTemplate.getForObject(String.format(Urls.GET_ORDER_BY_ID.getUrl(), id), OrderDTO.class));
        return "order/updateCommentInOrder";
    }

    @PatchMapping("/{id}/update-comment")
    public String updateComment(@PathVariable("id") int id,
                                @ModelAttribute("order") OrderDTO orderDTO,
                                @RequestParam(value = "client-short-name", required = false) String clientShortName,
                                @RequestParam(value = "created-by", required = false) String createdBy,
                                @RequestParam(value = "comment", required = false) String comment) {
        orderDTO.setCreatedBy(createdBy);
        orderDTO.setComment(comment);
        orderDTO.setClientShortName(clientShortName);
        orderDTO.setUpdatedBy(currentUser.getEmail());
        restTemplate.patchForObject(String.format(Urls.UPDATE_ORDER.getUrl(), id), orderDTO, HttpStatus.class);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_PRODUCTS.getUrl(), ProductDTOResponse.class)).getResponse());
        return "order/updateProductInOrder";
    }

    @PatchMapping("/{id}/update-product-quantity")
    public String updateProductQuantityInOrder(@PathVariable("id") int orderId,
                                               @RequestParam(value = "product-id", required = false) Integer productId,
                                               @RequestParam(value = "quantity", required = false) Integer quantity, Model model) {
        try {
            restTemplate.patchForObject(String.format(Urls.UPDATE_PRODUCT_QUANTITY_IN_ORDER.getUrl(), orderId, productId, quantity), null, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            model.addAttribute("error", 1);
            model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_PRODUCTS.getUrl(), ProductDTOResponse.class)).getResponse());
            return "order/updateProductInOrder";
        }
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}/delete-from-order")
    public String deleteProductInOrderByOrderIdAndProductId(@PathVariable("id") int orderId, @RequestParam(value = "product-id", required = false) Integer productId) {
        restTemplate.exchange(String.format(Urls.DELETE_PRODUCT_IN_ORDER_BY_ORDER_ID_AND_PRODUCT_ID.getUrl(), orderId, productId), HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}/delete-products")
    public String deleteAllProductsInOrder(@PathVariable("id") int orderId) {
        restTemplate.exchange(String.format(Urls.DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID.getUrl(), orderId), HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/orders/" + orderId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange(String.format(Urls.DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID.getUrl(), id), HttpMethod.DELETE, null, HttpStatus.class);
        restTemplate.exchange(String.format(Urls.DELETE_ORDER.getUrl(), id), HttpMethod.DELETE, null, HttpStatus.class);

        return "redirect:/orders";
    }

    @GetMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_PRODUCTS.getUrl(), ProductDTOResponse.class)).getResponse());
        return "order/addProductToOrder";
    }


    @PostMapping("/{id}/add-product")
    public String addProductToOrder(@PathVariable("id") int id,
                                    @RequestParam(value = "product-id", required = false) Integer productId,
                                    @RequestParam(value = "quantity", required = false) Integer quantity, Model model) {
        try {
            restTemplate.postForObject(String.format(Urls.ADD_PRODUCT_TO_ORDER.getUrl(), id, productId, quantity), null, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            model.addAttribute("error", 1);
            model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_PRODUCTS.getUrl(), ProductDTOResponse.class)).getResponse());
            return "order/addProductToOrder";
        }
        return "redirect:/orders/" + id;
    }
}
