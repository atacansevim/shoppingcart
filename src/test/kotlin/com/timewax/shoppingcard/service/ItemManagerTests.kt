package com.timewax.shoppingcard.service

import com.timewax.shoppingcard.model.Item
import com.timewax.shoppingcard.util.exception.ItemAlreadyAddedException
import com.timewax.shoppingcard.util.exception.ItemNotFoundException
import com.timewax.shoppingcard.util.exception.ItemSKUMissMatchException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ItemManagerServiceTest {

    private lateinit var itemManagerService: ItemManagerService

    @BeforeEach
    fun setUp() {
        itemManagerService = ItemManagerService()
    }

    @Test
    fun `test get item`() {
        val item = Item("SKU1", "Test Item", 10.0)

        itemManagerService.addItem(item)
        val retrievedItem = itemManagerService.getItem("SKU1")

        assertEquals(item, retrievedItem)
    }

    @Test
    fun `test get non-existent item`() {
        assertFailsWith<ItemNotFoundException> {
            itemManagerService.getItem("nonexistent")
        }
    }

    @Test
    fun `test add item`() {
        val item = Item("SKU1", "Test Item", 10.0)

        itemManagerService.addItem(item)
        val retrievedItem = itemManagerService.getItem("SKU1")

        assertEquals(item, retrievedItem)
    }

    @Test
    fun `test add exsisting item`() {
        val item = Item("SKU1", "Test Item", 10.0)

        itemManagerService.addItem(item)

        assertFailsWith<ItemAlreadyAddedException> {
            itemManagerService.addItem(item)
        }
    }

    @Test
    fun `test remove item`() {
        val item = Item("SKU1", "Test Item", 10.0)

        itemManagerService.addItem(item)
        itemManagerService.deleteItem("SKU1")

        assertFailsWith<ItemNotFoundException> {
            itemManagerService.getItem("SKU1")
        }
    }

    @Test
    fun `test update item`() {
        val item = Item("SKU1", "Test Item", 10.0)
        val updatedItem = Item("SKU1", "Test Item", 20.0)

        itemManagerService.addItem(item)
        itemManagerService.updateItem("SKU1", updatedItem)

        val retrievedItem = itemManagerService.getItem("SKU1")

        assertEquals(retrievedItem, updatedItem)
    }

    @Test
    fun `test update item with missmatch sku`() {
        val item = Item("SKU1", "Test Item", 10.0)
        val item2 =  Item("SKU2", "Test Item", 10.0)
        val updatedItem = Item("SKU1", "Test Item", 20.0)

        itemManagerService.addItem(item)
        itemManagerService.addItem(item2)

        assertFailsWith<ItemSKUMissMatchException> {
            itemManagerService.updateItem("SKU2", updatedItem)
        }
    }
}