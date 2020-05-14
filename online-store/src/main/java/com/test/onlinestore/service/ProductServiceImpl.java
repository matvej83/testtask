package com.test.onlinestore.service;

import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findProductById(id);
    }

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(Product product, Long id) throws RecordNotFoundException {
        productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No user record exist for given id!"));
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) throws RecordNotFoundException {
        productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No record exists for given id!"));
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteProductByName(String name) throws RecordNotFoundException {
        productRepository.findByName(name);
//                .orElseThrow(() -> new RecordNotFoundException("No record exists for given name!"));
        productRepository.deleteByName(name);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findAllByPriceBetween(Double minPrice, Double maxPrice) {
        return productRepository.findAllByPriceBetween(minPrice, maxPrice);
    }

}
