package com.example.inventoryService.services;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.repositoryes.ProductRepository;
import com.example.inventoryService.util.ModelMapperUtil;
import com.example.inventoryService.util.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(modelMapperUtil::convertProductToProductDTO)
                .collect(Collectors.toList());

        return createProductDTOResponse(productDTOs);
    }

    @Transactional(readOnly = true)
    public ProductDTOResponse findById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        ProductDTO productDTO = modelMapperUtil.convertProductToProductDTO(product);

        return createProductDTOResponse(Collections.singletonList(productDTO));
    }

    public void save(ProductDTO productDTO) {
        Product product = modelMapperUtil.convertProductDTOToProduct(productDTO);
        Optional<Product> productFromDb = productRepository.findByName(product.getName());
        if (productFromDb.isPresent()) {
            Product existingProduct = productFromDb.get();
            existingProduct.setUpdatedAt(new Date());
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        } else {
            product.setCreatedAt(new Date());
            productRepository.save(product);
        }
    }

    public void update(ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getId())
                .orElseThrow(ProductNotFoundException::new);
        productDTO.setCreatedAt(existingProduct.getCreatedAt());
        Product updatedProduct = modelMapperUtil.convertProductDTOToProduct(productDTO);
        updatedProduct.setUpdatedAt(new Date());
        productRepository.save(updatedProduct);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    private ProductDTOResponse createProductDTOResponse(List<ProductDTO> productDTOs) {
        ProductDTOResponse productDTOResponse = new ProductDTOResponse();
        productDTOResponse.setResponse(productDTOs);
        return productDTOResponse;
    }
}

