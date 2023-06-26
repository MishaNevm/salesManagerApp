package com.example.inventoryService.util;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.models.Product;
import com.example.inventoryService.models.ProductOrder;
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

    public ProductOrderDTO convertProductOrderToProductOrderDTO(ProductOrder productOrder) {
        ProductOrderDTO productOrderDTO = modelMapper.map(productOrder, ProductOrderDTO.class);
        if (productOrder.getProduct() != null) {
            productOrderDTO.setProduct(convertProductToProductDTO(productOrder.getProduct()));
        }
        return productOrderDTO;
    }
    public ProductOrder convertProductOrderDTOToProductOrder(ProductOrderDTO productOrderDTO) {
        ProductOrder productOrder = modelMapper.map(productOrderDTO, ProductOrder.class);
        if (productOrderDTO.getProduct() != null) {
            productOrder.setProduct(convertProductDTOToProduct(productOrderDTO.getProduct()));
        }
        return productOrder;
    }


}
