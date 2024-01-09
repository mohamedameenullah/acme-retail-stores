package com.acme.retail.acmeretailstores.service;

import com.acme.retail.acmeretailstores.model.Product;
import com.acme.retail.acmeretailstores.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(Product product) {
        if (product.getDate() == null) {
            product.setDate(new Date());
        }
        return productRepository.save(product);
    }

    public List<Product> findALl() {
        return productRepository.findAll();

    }

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Product updateProduct(long id, Product product) {
        Product foundProduct = findById(id);
        foundProduct.setDate(product.getDate());
        foundProduct.setName(product.getName());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setSku(product.getSku());
        return productRepository.save(foundProduct);
    }
}
