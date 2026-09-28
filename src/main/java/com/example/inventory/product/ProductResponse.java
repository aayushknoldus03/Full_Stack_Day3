package com.example.inventory.product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String sku, String category, BigDecimal price, int quantity) {
	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getSku(),
				product.getCategory(),
				product.getPrice(),
				product.getQuantity());
	}
}