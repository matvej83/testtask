package com.test.onlinestore.service;

import com.test.onlinestore.entity.ProductsInCart;

import java.util.List;

public interface ProductsInCartService {

    List<ProductsInCart> findAllProductsInCart();

}
