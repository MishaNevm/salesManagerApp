package com.example.Frontend.controllers;


import com.example.Frontend.dto.ProductDTO;
import com.example.Frontend.dto.ProductDTOResponse;
import com.example.Frontend.util.CurrentUser;
import com.example.Frontend.util.ProductTypes;
import com.example.Frontend.util.Urls;
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
    private final CurrentUser currentUser;

    @Autowired
    public ProductController(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("products",
                Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_PRODUCTS.getUrl(),
                        ProductDTOResponse.class))
                        .getResponse());
        return "inventory/getAllProducts";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("product",
                Objects.requireNonNull(restTemplate.getForObject(String.format(Urls.GET_PRODUCT_BY_ID.getUrl(), id),
                        ProductDTO.class)));
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
        productDTO.setCreatedBy(currentUser.getEmail());
        restTemplate.postForObject(Urls.CREATE_PRODUCT.getUrl(), productDTO, HttpStatus.class);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        model.addAttribute("types", ProductTypes.values());
        model.addAttribute("product",
                Objects.requireNonNull(restTemplate.getForObject(String.format(Urls.GET_PRODUCT_BY_ID.getUrl(), id),
                        ProductDTO.class)));
        return "inventory/updateProduct";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("product") @Valid ProductDTO productDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "inventory/updateProduct";
        }
        productDTO.setUpdatedBy(currentUser.getEmail());
        restTemplate.patchForObject(String.format(Urls.UPDATE_PRODUCT.getUrl(), id), productDTO, HttpStatus.class);
        return "redirect:/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange( String.format(Urls.DELETE_PRODUCT.getUrl(), id),
                HttpMethod.DELETE,
                null,
                HttpStatus.class);
        return "redirect:/products";
    }
}
