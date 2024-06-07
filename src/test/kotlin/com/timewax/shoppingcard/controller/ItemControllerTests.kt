package com.timewax.shoppingcard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.timewax.shoppingcard.model.Item
import com.timewax.shoppingcard.service.ItemManagerService
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @MockBean
    private lateinit var itemManagerService: ItemManagerService

    @BeforeEach
    fun setUp() {
        itemManagerService = mockk<ItemManagerService>(relaxed = true)
    }

    @Test
    fun `when add item request body correct should be return CREATED`() {
        val item = Item("SKUTEST", "displayName", 10.0)

        every { itemManagerService.addItem(any()) } returns Unit

        val performPost = mockMvc.post("/api/v1/items/add") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
        }

        performPost.andExpect {
            status {
                isCreated()
            }
        }
    }

    @Test
    fun `when item's sku is empty should be return BadRequest and the message`() {

        val item = Item("", "displayName", 10.0)

        every { itemManagerService.addItem(any()) } returns Unit


        val performPost = mockMvc.post("/api/v1/items/add") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
        }

        performPost.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("The sku must not be blank") }
            }
        }
    }

    @Test
    fun `when item's displayName is empty should be return BadRequest and the message`() {

        val item = Item("SKU", "", 10.0)

        every { itemManagerService.addItem(any()) } returns Unit

        val performPost = mockMvc.post("/api/v1/items/add") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
        }

        performPost.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("The displayName must not be blank") }
            }
        }
    }

    @Test
    fun `when item's price is lower than 1 should be return BadRequest and the message`() {

        val item = Item("SKU", "displayName", 0.0)

        every { itemManagerService.addItem(any()) } returns Unit

        val performPost = mockMvc.post("/api/v1/items/add") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
        }


        performPost.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("The price must be positive") }
            }
        }
    }

    @Test
    fun `when item deleted with valid SKU should return OK`() {
        val sku = "SKU123"

        every { itemManagerService.deleteItem(any()) } returns Unit

        val performDelete = mockMvc.delete("/api/v1/items/${sku}") {
            param("sku", sku)
        }

        performDelete.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Item deleted successfully") }
            }
        }
    }

    @Test
    fun `when update item quantity with valid SKU and quantity should return OK`() {
        val sku = "SKU123"
        val item = Item("SKU123", "displayName", 10.0)

        every { itemManagerService.updateItem(any(), any()) } returns Unit

        val performPut = mockMvc.put("/api/v1/items/${sku}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(item)
            param("sku", sku)
        }

        performPut.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.valueOf("text/plain;charset=UTF-8"))
                jsonPath("$") { value("Item updated successfully") }
            }
        }
    }

}