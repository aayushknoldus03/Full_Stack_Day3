package com.example.inventory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);

	Page<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
			String name, String category, Pageable pageable);

	@Query("SELECT product FROM Product product WHERE product.quantity <= :threshold")
	Page<Product> findLowStock(@Param("threshold") int threshold, Pageable pageable);
}