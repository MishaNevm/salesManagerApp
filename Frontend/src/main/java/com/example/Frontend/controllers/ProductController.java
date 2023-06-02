package com.example.Frontend.controllers;


import com.example.Frontend.dto.ProductDTO;
import com.example.Frontend.dto.ProductDTOResponse;
import com.example.Frontend.dto.UserDTO;
import com.example.Frontend.util.ProductTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final RestTemplate restTemplate;
    private final String GET_ALL_PRODUCTS = "http://localhost:8484/products";
    private final String GET_PRODUCT_BY_ID = "http://localhost:8484/products/";
    private final String CREATE_PRODUCT = GET_ALL_PRODUCTS;
    private final String UPDATE_PRODUCT = GET_PRODUCT_BY_ID;
    private final String DELETE_PRODUCT = GET_PRODUCT_BY_ID;

    @Autowired
    public ProductController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String findAll(Model model) throws InterruptedException {
        model.addAttribute("products", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_PRODUCTS, ProductDTOResponse.class)).getResponse());
        return "inventory/getAllProducts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", Objects.requireNonNull(restTemplate.getForObject(GET_PRODUCT_BY_ID + id, ProductDTO.class)));
        return "inventory/getProductById";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("types", ProductTypes.values());
        return "inventory/createProduct";
    }

    @PostMapping
    public String create(@ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inventory/createProduct";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDTO> entity = new HttpEntity<>(productDTO, headers);
        restTemplate.exchange(CREATE_PRODUCT, HttpMethod.POST, entity, HttpStatus.class);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        model.addAttribute("types", ProductTypes.values());
        model.addAttribute("product", Objects.requireNonNull(restTemplate.getForObject(GET_PRODUCT_BY_ID + id, ProductDTO.class)));
        return "inventory/updateProduct";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inventory/updateProduct";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDTO> entity = new HttpEntity<>(productDTO, headers);
        restTemplate.exchange(UPDATE_PRODUCT + id, HttpMethod.PATCH, entity, HttpStatus.class);
        return "redirect:/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange(DELETE_PRODUCT + id, HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/products";
    }

//    @PostMapping("/{id}/add-to-order")
//    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
//                                                        @RequestParam(value = "order-id", required = false) Integer orderId,
//                                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
//        productService.addProductToOrder(productService.findById(id), orderId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

//    @PatchMapping("/{id}/change-order")
//    public ResponseEntity<HttpStatus> changeOrderInProduct(@PathVariable("id") int id,
//                                                           @RequestParam(value = "old-order-id", required = false) Integer oldOrderId,
//                                                           @RequestParam(value = "new-order-id", required = false) Integer newOrderId) {
//        productService.findById(id);
////        productService.changeOrderInProduct(id, oldOrderId, newOrderId);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotSaveException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Товар не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotAddException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
}
