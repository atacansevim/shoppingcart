package com.timewax.shoppingcard.util.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

open class ShoppingCartException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class ItemNotFoundInCartException(sku: String) : ShoppingCartException("Item with SKU $sku not found in the cart")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ItemAlreadyInCartException : ShoppingCartException("The item is already exist")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CartAlreadyEmptyException : ShoppingCartException("The cart is already empty")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidDiscountCodeException(discountCode: String) : ShoppingCartException("The $discountCode is invalid")