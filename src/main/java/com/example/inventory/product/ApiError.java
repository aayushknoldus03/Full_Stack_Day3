package com.example.inventory.product;

import java.time.Instant;

public record ApiError(Instant timestamp, int status, String error, String message, String path) {
}