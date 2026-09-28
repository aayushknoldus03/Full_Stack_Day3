package com.example.inventory.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public ProductResponse create(ProductRequest request) {
		Product product = new Product(
				request.name().trim(),
				request.sku().trim(),
				request.category().trim(),
				request.price(),
				request.quantity());
		return ProductResponse.from(productRepository.save(product));
	}

	@Transactional(readOnly = true)
	public Page<ProductResponse> findAll(String name, String category, Pageable pageable) {
		String nameFilter = name == null ? "" : name.trim();
		String categoryFilter = category == null ? "" : category.trim();
		Page<Product> products;

		if (nameFilter.isEmpty() && categoryFilter.isEmpty()) {
			products = productRepository.findAll(pageable);
		} else if (nameFilter.isEmpty()) {
			products = productRepository.findByCategoryIgnoreCase(categoryFilter, pageable);
		} else if (categoryFilter.isEmpty()) {
			products = productRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
		} else {
			products = productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
					nameFilter, categoryFilter, pageable);
		}

		return products.map(ProductResponse::from);
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return ProductResponse.from(getProduct(id));
	}

	public ProductResponse update(Long id, ProductRequest request) {
		Product product = getProduct(id);
		product.update(
				request.name().trim(),
				request.sku().trim(),
				request.category().trim(),
				request.price(),
				request.quantity());
		return ProductResponse.from(productRepository.save(product));
	}

	public void delete(Long id) {
		productRepository.delete(getProduct(id));
	}

	@Transactional(readOnly = true)
	public Page<ProductResponse> findLowStock(int threshold, Pageable pageable) {
		return productRepository.findLowStock(threshold, pageable).map(ProductResponse::from);
	}

	private Product getProduct(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}
}