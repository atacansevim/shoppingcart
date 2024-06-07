package com.timewax.shoppingcard.service

import com.timewax.shoppingcard.logger.CartEventLoggerService
import com.timewax.shoppingcard.model.response.GetItemsResponse
import com.timewax.shoppingcard.util.exception.CartAlreadyEmptyException
import com.timewax.shoppingcard.util.CartEventEnum
import com.timewax.shoppingcard.util.exception.InvalidDiscountCodeException
import com.timewax.shoppingcard.util.exception.ItemAlreadyInCartException
import com.timewax.shoppingcard.util.exception.ItemNotFoundInCartException
import org.springframework.stereotype.Service

@Service
class ShoppingCartService (
    private val cardEventLoggerService: CartEventLoggerService,
    private val itemManagerService: ItemManagerService
) {
    private val cartItems = mutableMapOf<String, Int>()
    private val discountCodes = mapOf(
        "DISCOUNT10" to 0.1,
        "DISCOUNT20" to 0.2
    )

    fun addItemToCart(sku: String, quantity: Int) {
        itemManagerService.getItem(sku)

        if (cartItems.containsKey(sku)) {
            throw ItemAlreadyInCartException()
        } else {
            cartItems[sku] = quantity
            cardEventLoggerService.logEvent(
                eventType = CartEventEnum.ADDED,
                details = "Item $sku added with quantity $quantity"
            )
        }
    }

    fun updateItemQuantity(sku: String, quantity: Int) {
        itemManagerService.getItem(sku)

        if (cartItems.containsKey(sku)) {
            cartItems[sku] = quantity
            cardEventLoggerService.logEvent(
                eventType = CartEventEnum.UPDATED_QUANTITY,
                details = "Item $sku updated to quantity $quantity"
            )
        }
    }

    fun removeItemFromCart(sku: String) {
        itemManagerService.getItem(sku)

        val item = cartItems.remove(sku)
        item?.let {
            cardEventLoggerService.logEvent(
                eventType = CartEventEnum.REMOVED,
                details = "Item $sku removed"
            )
            return
        }
        throw ItemNotFoundInCartException(sku = sku)
    }

    fun emptyCart() {
        if (cartItems.isEmpty()) {
            throw CartAlreadyEmptyException()
        }
        cartItems.clear()
        cardEventLoggerService.logEvent(
            eventType = CartEventEnum.EMPTY,
            details = "All items removed from the cart"
        )
    }

    fun getCartItems(): List<GetItemsResponse> {
        val itemsWithQuantity = mutableListOf<GetItemsResponse>()
        for ((sku, quantity) in cartItems) {
            val item = itemManagerService.getItem(sku)
            if (item != null) {
                itemsWithQuantity.add(GetItemsResponse(item, quantity))
            }
        }
        return itemsWithQuantity
    }

    fun calculateTotal(discountCode: String?): Double {
        val totalAmount = getTotal()
        discountCode?.let {
            val discountPercentage = checkDiscountCode(discountCode)
            return totalAmount - (totalAmount * discountPercentage)
        }
        return totalAmount
    }

    private fun checkDiscountCode(discountCode: String): Double {
        discountCodes[discountCode]?.let {
            return it
        }
        throw InvalidDiscountCodeException(discountCode)
    }

    private fun getTotal(): Double {
        var totalAmountOfCart: Double = 0.0
        for ((sku, quantity) in cartItems) {
            totalAmountOfCart += calculateAmountOfCart(sku, quantity)
        }
        return totalAmountOfCart
    }

    private fun calculateAmountOfCart(sku: String, quantity: Int): Double {
        val item = itemManagerService.getItem(sku)
        return item?.price?.times(quantity) ?: 0.0
    }
}