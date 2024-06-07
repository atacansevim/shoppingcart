package com.timewax.shoppingcard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.timewax.shoppingcard.model.request.AddItemToCartRequest
import com.timewax.shoppingcard.model.response.GetItemsResponse
import com.timewax.shoppingcard.service.ShoppingCartService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var shoppingCartService: ShoppingCartService

    @BeforeEach
    fun setUp() {
        shoppingCartService = mockk<ShoppingCartService>(relaxed = true)
    }


    @Test
    fun `when add item to cart request body correct should return OK`() {
        val addRequest = AddItemToCartRequest("SKUTEST", 5)

        every { shoppingCartService.addItemToCart("SKUTEST", 5) } returns Unit

        val performPost = mockMvc.post("/api/v1/carts/add") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(addRequest)
        }

        performPost.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Item added to cart") }
            }
        }
    }

    @Test
    fun `when remove item from cart with valid SKU should return OK`() {
        val sku = "SKU123"

        every { shoppingCartService.removeItemFromCart(any()) } returns Unit

        val performDelete = mockMvc.delete("/api/v1/carts/remove") {
            param("sku", sku)
        }

        performDelete.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Item removed from cart") }
            }
        }
    }

    @Test
    fun `when update item quantity with valid SKU and quantity should return OK`() {
        val request = AddItemToCartRequest("SKU100", 10)

        every { shoppingCartService.updateItemQuantity(any(), any()) } returns Unit

        val performPut = mockMvc.put("/api/v1/carts/update") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }

        performPut.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Item quantity updated") }
            }
        }
    }

    @Test
    fun `when update item quantity with valid SKU and invalid much quantity should return BadRequest`() {
        val request = AddItemToCartRequest("SKU100", 1001)

        every { shoppingCartService.updateItemQuantity(any(), any()) } returns Unit

        val performPut = mockMvc.put("/api/v1/carts/update") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }

        performPut.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("The quantity must be equal or less than 1000") }
            }
        }
    }

    @Test
    fun `when update item quantity with valid SKU and invalid less quantity should return BadRequest`() {
        val request = AddItemToCartRequest("SKU100", 0)

        every { shoppingCartService.updateItemQuantity(any(), any()) } returns Unit

        val performPut = mockMvc.put("/api/v1/carts/update") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }

        performPut.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("The quantity must be positive") }
            }
        }
    }

    @Test
    fun `when empty item from cart should return OK`() {
        every { shoppingCartService.emptyCart() } returns Unit

        val performPut = mockMvc.post("/api/v1/carts/empty")

        performPut.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Cart emptied") }
            }
        }
    }

    @Test
    fun `when get items from cart with valid SKU should return OK`() {
        every { shoppingCartService.getCartItems() } returns emptyList<GetItemsResponse>()

        val performPut = mockMvc.get("/api/v1/carts/items")

        performPut.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun `when get totals should return OK`() {
        every { shoppingCartService.calculateTotal(any()) } returns 10.0

        val performPut = mockMvc.get("/api/v1/carts/total")

        performPut.andExpect {
            status {
                isOk()
            }
        }
    }
}
