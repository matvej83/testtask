package com.test.onlinestore.controller;

import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = {"product"})
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @ApiOperation(value = "Get product by id", response = Product.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Product getProductById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        return productRepository.findProductById(id);
    }

    @ApiOperation(value = "Add new product")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public String add(@RequestParam String name, @RequestParam Double price, @RequestParam String ean,
                      Map<String, Object> model) {
        Product product = new Product(name, price, ean);
        productRepository.save(product);
        Iterable<Product> products = productRepository.findAll();
        model.put("products", products);
        return "test";
    }

    @ApiOperation(value = "Get all products")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Product> getAllProducts()
            throws RecordNotFoundException {
        return productRepository.findAll();
    }

    @ApiOperation(value = "Update a product", response = Product.class)
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, produces = "application/json")
    public boolean updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product storedProduct = productRepository.findProductById(id);
        storedProduct.setName(product.getName());
        storedProduct.setPrice(product.getPrice());
        storedProduct.setEan(product.getEan());
        productRepository.save(storedProduct);
        return true;
    }

    @ApiOperation(value = "Delete a product")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public boolean delete(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return true;
    }

}
