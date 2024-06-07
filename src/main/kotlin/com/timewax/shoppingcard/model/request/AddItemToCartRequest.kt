package com.timewax.shoppingcard.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class AddItemToCartRequest(
    @Schema(
        name = "sku",
        description = "Stock Keeping Unit",
        type = "String",
        example = "SKU001"
    )
    @field:NotEmpty(message = "The SKU must not be blank")
    val sku: String,
    @Schema(
        name = "quantity",
        description = "The number of items",
        type = "Integer",
        example = "2"
    )
    @field:Min(1, message = "The quantity must be positive")
    @field:Max(1000, message = "The quantity must be equal or less than 1000")
    val quantity: Int
)