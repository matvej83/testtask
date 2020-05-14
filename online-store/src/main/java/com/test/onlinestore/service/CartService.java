package com.test.onlinestore.service;

import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;

import java.util.List;

public interface CartService {

    Cart getCartById(Long id);

    void createCart(Cart cart);

    void updateCart(Cart cart, Long id) throws RecordNotFoundException;

    List<Product> getProductInCart();

}
