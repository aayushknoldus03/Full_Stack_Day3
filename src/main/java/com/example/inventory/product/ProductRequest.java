package com.example.inventory.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
		@NotBlank @Size(max = 120) String name,
		@NotBlank @Size(max = 40) String sku,
		@NotBlank @Size(max = 80) String category,
		@NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) BigDecimal price,
		@Min(0) int quantity) {
}