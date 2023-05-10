package com.example.inventoryService.util;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Product convertProductDTOToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    public ProductDTO convertProductToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

}
