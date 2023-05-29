package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.ProductDTO;
import com.example.gatewayService.dto.ProductDTOResponse;
import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import com.example.gatewayService.util.ProductTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final Consumer consumer;
    private final Producer producer;

    @Autowired

    public ProductController(Consumer consumer, Producer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }


    @GetMapping
    public String findAll(Model model) throws InterruptedException {
        producer.sendRequestToInventoryService(MethodsCodes.GET_ALL_PRODUCTS, new ProductDTO());
        model.addAttribute("products", consumer.getResponseMap().get(MethodsCodes.GET_ALL_PRODUCTS).take().getResponse());
        return "inventory/getAllProducts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCT_BY_ID, productDTO);
        model.addAttribute("product", consumer.getResponseMap().get(MethodsCodes.GET_PRODUCT_BY_ID).take().getResponse().get(0));
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
        producer.sendRequestToInventoryService(MethodsCodes.CREATE_PRODUCT, productDTO);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCT_BY_ID, productDTO);
        model.addAttribute("types", ProductTypes.values());
        model.addAttribute("product", consumer.getResponseMap().get(MethodsCodes.GET_PRODUCT_BY_ID).take().getResponse().get(0));
        return "inventory/updateProduct";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inventory/updateProduct";
        }
        producer.sendRequestToInventoryService(MethodsCodes.UPDATE_PRODUCT, productDTO);
        return "redirect:/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_PRODUCT, productDTO);
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
