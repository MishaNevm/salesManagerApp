package com.example.inventoryService.controllers;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.services.ProductService;
import com.example.inventoryService.util.ErrorResponse;
import com.example.inventoryService.util.ModelMapperUtil;
import com.example.inventoryService.util.ProductNotAddException;
import com.example.inventoryService.util.ProductNotFoundException;
import com.example.inventoryService.util.ProductNotSaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ModelMapperUtil modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapperUtil modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAll().stream().map(modelMapper::convertProductToProductDTO).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") int id) {
        return modelMapper.convertProductToProductDTO(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ProductNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        productService.save(modelMapper.convertProductDTOToProduct(productDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody @Valid ProductDTO productDTO,
                                             BindingResult bindingResult) {
        productDTO.setCreatedAt(productService.findById(id).getCreatedAt());
        productDTO.setId(id);
        if (bindingResult.hasErrors()) {
            throw new ProductNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        productService.update(modelMapper.convertProductDTOToProduct(productDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        productService.findById(id);
        productService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/add-to-order")
    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
                                                        @RequestParam(value = "order-id", required = false) Integer orderId,
                                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
        productService.addProductToOrder(productService.findById(id), orderId, quantity);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/change-order")
    public ResponseEntity<HttpStatus> changeOrderInProduct(@PathVariable("id") int id,
                                                           @RequestParam(value = "old-order-id", required = false) Integer oldOrderId,
                                                           @RequestParam(value = "new-order-id", required = false) Integer newOrderId) {
        productService.findById(id);
//        productService.changeOrderInProduct(id, oldOrderId, newOrderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotSaveException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Товар не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotAddException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
