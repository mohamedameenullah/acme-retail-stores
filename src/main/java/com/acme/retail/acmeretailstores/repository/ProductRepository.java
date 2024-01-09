package com.acme.retail.acmeretailstores.repository;

import com.acme.retail.acmeretailstores.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
