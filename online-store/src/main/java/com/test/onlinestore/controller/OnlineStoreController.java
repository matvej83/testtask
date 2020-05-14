package com.test.onlinestore.controller;

import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@AllArgsConstructor
public class OnlineStoreController {

    private final ProductRepository productRepository;

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);
        return "test";
    }


    @GetMapping("/filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) throws RecordNotFoundException {
        Product products;
        products = productRepository.findProductById(Long.valueOf(filter));
        model.put("products", products);
        return "test";
    }

}