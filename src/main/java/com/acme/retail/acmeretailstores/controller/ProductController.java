package com.acme.retail.acmeretailstores.controller;

import com.acme.retail.acmeretailstores.model.Product;
import com.acme.retail.acmeretailstores.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct =  productService.create(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<Product>> fetchAll() {
        List<Product> productList =  productService.findALl();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Product> fetchById(@PathVariable long id) {
        Product foundProduct =  productService.findById(id);
        return new ResponseEntity<>(foundProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Product> fetchById(@PathVariable long id, @RequestBody Product product) {
        Product updatedProduct =  productService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}
