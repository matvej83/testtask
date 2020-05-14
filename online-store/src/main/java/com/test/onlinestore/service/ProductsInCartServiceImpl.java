package com.test.onlinestore.service;

import com.test.onlinestore.entity.ProductsInCart;
import com.test.onlinestore.repository.ProductsInCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductsInCartServiceImpl implements ProductsInCartService {

    private final ProductsInCartRepository productsInCartRepository;

    @Override
    public List<ProductsInCart> findAllProductsInCart() {
        return productsInCartRepository.findAll();
    }
}
