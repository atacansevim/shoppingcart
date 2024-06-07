package com.timewax.shoppingcard.controller

import com.timewax.shoppingcard.model.request.AddItemToCartRequest
import com.timewax.shoppingcard.model.response.GetItemsResponse
import com.timewax.shoppingcard.service.ShoppingCartService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/carts")
class ShoppingCartController(
    private val shoppingCartService: ShoppingCartService
) {

    @PostMapping("/add")
    fun addItemToCart(@Valid @RequestBody addRequest: AddItemToCartRequest): ResponseEntity<String> {
        shoppingCartService.addItemToCart(addRequest.sku, addRequest.quantity)
        return ResponseEntity.ok("Item added to cart")
    }

    @DeleteMapping("/remove")
    fun removeItemFromCart(@RequestParam sku: String): ResponseEntity<String> {
        shoppingCartService.removeItemFromCart(sku)
        return ResponseEntity.ok("Item removed from cart")
    }

    @PutMapping("/update")
    fun updateItemQuantity(@Valid @RequestBody addRequest: AddItemToCartRequest): ResponseEntity<String> {
        shoppingCartService.updateItemQuantity(addRequest.sku, addRequest.quantity)
        return ResponseEntity.ok("Item quantity updated")
    }

    @PostMapping("/empty")
    fun emptyCart(): ResponseEntity<String> {
        shoppingCartService.emptyCart()
        return ResponseEntity.ok("Cart emptied")
    }

    @GetMapping("/items")
    fun getCartItems(): ResponseEntity<List<GetItemsResponse>> {
        val cartItems = shoppingCartService.getCartItems()
        return ResponseEntity.ok(cartItems)
    }

    @GetMapping("/total")
    fun getTotal(@RequestParam(required = false) discountCode: String?): ResponseEntity<Double> {
        val totalWithDiscount = shoppingCartService.calculateTotal(discountCode)
        return ResponseEntity.ok(totalWithDiscount)
    }
}