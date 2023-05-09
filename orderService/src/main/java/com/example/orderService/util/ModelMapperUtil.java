package com.example.orderService.util;

import com.example.salesManagerApp.dto.*;
import com.example.salesManagerApp.models.*;
import com.example.salesManagerApp.services.ProductOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapperUtil {

    private final ProductOrderService productOrderService;

    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ProductOrderService productOrderService, ModelMapper modelMapper) {
        this.productOrderService = productOrderService;
        this.modelMapper = modelMapper;
    }

    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User convertUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public Client convertClientDTOToClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        if (clientDTO.getBankDTO() != null) {
            client.setBank(modelMapper.map(clientDTO.getBankDTO(), Bank.class));
        }
        return client;
    }

    public ClientDTO convertClientToClientDTO(Client client) {
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        if (client.getBank() != null) {
            clientDTO.setBankDTO(modelMapper.map(client.getBank(), BankDTO.class));
        }
        return clientDTO;
    }

    public Bank convertBankDTOToBank(BankDTO bankDTO) {
        Bank bank = modelMapper.map(bankDTO, Bank.class);
        if (bankDTO.getClientDTO() != null) {
            bank.setClient(modelMapper.map(bankDTO.getClientDTO(), Client.class));
        }
        return bank;
    }

    public BankDTO convertBankToBankDTO(Bank bank) {
        BankDTO bankDTO = modelMapper.map(bank, BankDTO.class);
        if (bank.getClient() != null) {
            bankDTO.setClientDTO(modelMapper.map(bank.getClient(), ClientDTO.class));
        }
        return bankDTO;
    }

    public Product convertProductDTOToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    public ProductDTO convertProductToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    public Order convertOrderDTOToOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        if (orderDTO.getClientDTO() != null) {
            order.setClient(convertClientDTOToClient(orderDTO.getClientDTO()));
        }
        return order;
    }

    public OrderDTO convertOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        List<ProductOrder> productOrderList = productOrderService.findByOrderId(order.getId());
        orderDTO.setProductDTOinOrderList(new ArrayList<>());
        for (ProductOrder productOrder : productOrderList) {
            ProductDTO productDTO = convertProductToProductDTO(productOrder.getProduct());
            productDTO.setQuantity(productOrder.getQuantity());
            orderDTO.getProductDTOinOrderList().add(productDTO);
        }
        if (order.getClient() != null) {
            orderDTO.setClientDTO(convertClientToClientDTO(order.getClient()));
        }
        return orderDTO;
    }
}
