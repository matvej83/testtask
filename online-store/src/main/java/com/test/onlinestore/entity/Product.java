package com.test.onlinestore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, unique = true)
    private String ean;

    @OneToMany(cascade = CascadeType.ALL)
    List<ProductsInCart> productsInCarts;

    public Product() {
    }

    public Product(String name, Double price, String ean) {
        this.name = name;
        this.price = price;
        this.ean = ean;
    }

}