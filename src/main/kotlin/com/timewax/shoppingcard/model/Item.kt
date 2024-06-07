package com.timewax.shoppingcard.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class Item(
    @Schema(
        name = "sku",
        description = "The unit of measure in which the stocks of a material are managed.",
        type = "String",
        example = "SKU001"
    )
    @field:NotEmpty(message = "The sku must not be blank")
    val sku: String,
    @Schema(
        name = "displayName",
        description = "The name of Item that is used for showing to users.",
        type = "String",
        example = "Basket Ball"
    )
    @field:NotEmpty(message = "The displayName must not be blank")
    val displayName: String,
    @field:Min(1, message = "The price must be positive")
    @Schema(
        name = "price",
        description = "The cost of the item",
        type = "Double",
        example = "10.00"
    )
    val price: Double
)