package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.repositoryes.ProductOrderRepository;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ModelMapperUtil;
import com.example.inventoryService.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapperUtil modelMapperUtil;

    public ProductService(ProductRepository productRepository, ModelMapperUtil modelMapperUtil) {
        this.productRepository = productRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findAll() {
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(productRepository.findAll().stream().map(modelMapperUtil::convertProductToProductDTO).toList());
        return productDTOResponse;
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findById(int id) {
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(Collections.singletonList(modelMapperUtil.convertProductToProductDTO(productRepository.findById(id).orElseThrow(ProductNotFoundException::new))));
        return productDTOResponse;
    }

    public void save(ProductDTO productDTO) {
        Product product = modelMapperUtil.convertProductDTOToProduct(productDTO);
        Optional<Product> productFromDb = productRepository.findByName(product.getName());
        if (productFromDb.isPresent()) {
            Product product2 = productFromDb.get();
            product2.setUpdatedAt(new Date());
            product2.setQuantity(productFromDb.get().getQuantity() + product.getQuantity());
        } else {
            product.setCreatedAt(new Date());
            productRepository.save(product);
        }
    }

    public void update(ProductDTO productDTO) {
        productDTO.setCreatedAt(productRepository.findById(productDTO.getId()).orElseThrow(ProductNotFoundException::new).getCreatedAt());
        Product product = modelMapperUtil.convertProductDTOToProduct(productDTO);
        product.setUpdatedAt(new Date());
        productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

}
