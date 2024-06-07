package com.timewax.shoppingcard.service

import com.timewax.shoppingcard.util.exception.*
import com.timewax.shoppingcard.logger.CartEventLoggerService
import com.timewax.shoppingcard.util.CartEventEnum
import com.timewax.shoppingcard.model.Item
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShoppingCartServiceTests {

    private lateinit var shoppingCartService: ShoppingCartService
    private val cardEventLoggerService = mockk<CartEventLoggerService>(relaxed = true)
    private val itemManagerService = mockk<ItemManagerService>(relaxed = true)

    @BeforeEach
    fun setUp() {
        shoppingCartService = ShoppingCartService(cardEventLoggerService, itemManagerService)
    }

    @Test
    fun `test add item to cart`() {
        val sku = "SKU1"
        val quantity = 2
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, quantity)

        verify { cardEventLoggerService.logEvent(CartEventEnum.ADDED, "Item $sku added with quantity $quantity") }
    }

    @Test
    fun `test add existing item to cart should throw exception`() {
        val sku = "SKU1"
        val quantity = 2
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, quantity)

        assertThrows<ItemAlreadyInCartException> {
            shoppingCartService.addItemToCart(sku, quantity)
        }
    }

    @Test
    fun `test update item quantity`() {
        val sku = "SKU1"
        val initialQuantity = 1
        val updatedQuantity = 5
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, initialQuantity)
        shoppingCartService.updateItemQuantity(sku, updatedQuantity)

        verify { cardEventLoggerService.logEvent(CartEventEnum.UPDATED_QUANTITY, "Item $sku updated to quantity $updatedQuantity") }
    }

    @Test
    fun `test remove item from cart`() {
        val sku = "SKU1"
        val quantity = 1
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, quantity)
        shoppingCartService.removeItemFromCart(sku)

        verify { cardEventLoggerService.logEvent(CartEventEnum.REMOVED, "Item $sku removed") }
    }

    @Test
    fun `test remove non-existent item from cart should throw exception`() {
        val sku = "SKU1"

        every { itemManagerService.getItem(sku) } returns null

        assertThrows<ItemNotFoundInCartException> {
            shoppingCartService.removeItemFromCart(sku)
        }
    }

    @Test
    fun `test empty cart`() {
        val sku = "SKU1"
        val quantity = 1
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, quantity)
        shoppingCartService.emptyCart()

        verify { cardEventLoggerService.logEvent(CartEventEnum.EMPTY, "All items removed from the cart") }
    }

    @Test
    fun `test empty already empty cart should throw exception`() {
        assertThrows<CartAlreadyEmptyException> {
            shoppingCartService.emptyCart()
        }
    }

    @Test
    fun `test get cart items`() {
        val sku = "SKU1"
        val quantity = 2
        val item = Item(sku, "Test Item", 10.0)

        every { itemManagerService.getItem(sku) } returns item

        shoppingCartService.addItemToCart(sku, quantity)

        val cartItems = shoppingCartService.getCartItems()
        assertEquals(1, cartItems.size)
        assertEquals(sku, cartItems[0].item.sku)
        assertEquals(quantity, cartItems[0].quantity)
    }

    @Test
    fun `test calculate total without discount`() {
        val sku1 = "SKU1"
        val sku2 = "SKU2"
        val item1 = Item(sku1, "Test Item 1", 10.0)
        val item2 = Item(sku2, "Test Item 2", 20.0)

        every { itemManagerService.getItem(sku1) } returns item1
        every { itemManagerService.getItem(sku2) } returns item2

        shoppingCartService.addItemToCart(sku1, 2)
        shoppingCartService.addItemToCart(sku2, 1)

        val total = shoppingCartService.calculateTotal(null)
        assertEquals(40.0, total)
    }

    @Test
    fun `test calculate total with discount`() {
        val sku1 = "SKU1"
        val sku2 = "SKU2"
        val item1 = Item(sku1, "Test Item 1", 10.0)
        val item2 = Item(sku2, "Test Item 2", 20.0)

        every { itemManagerService.getItem(sku1) } returns item1
        every { itemManagerService.getItem(sku2) } returns item2

        shoppingCartService.addItemToCart(sku1, 2)
        shoppingCartService.addItemToCart(sku2, 1)

        val totalWithDiscount = shoppingCartService.calculateTotal("DISCOUNT10")
        assertEquals(36.0, totalWithDiscount)
    }

    @Test
    fun `test calculate total with invalid discount code`() {
        val sku1 = "SKU1"
        val sku2 = "SKU2"
        val item1 = Item(sku1, "Test Item 1", 10.0)
        val item2 = Item(sku2, "Test Item 2", 20.0)

        every { itemManagerService.getItem(sku1) } returns item1
        every { itemManagerService.getItem(sku2) } returns item2

        shoppingCartService.addItemToCart(sku1, 2) // 2 * 10.0 = 20.0
        shoppingCartService.addItemToCart(sku2, 1) // 1 * 20.0 = 20.0

        assertThrows<InvalidDiscountCodeException> {
            shoppingCartService.calculateTotal("INVALID")
        }
    }
}
