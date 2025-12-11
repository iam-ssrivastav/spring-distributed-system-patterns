package com.shivamsrivastav.distributedpatterns.cqrs.repository;

import com.shivamsrivastav.distributedpatterns.cqrs.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Product Write Model.
 *
 * @author Shivam Srivastav
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
