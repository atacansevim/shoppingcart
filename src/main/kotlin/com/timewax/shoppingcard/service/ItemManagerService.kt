package com.timewax.shoppingcard.service

import com.timewax.shoppingcard.model.Item
import com.timewax.shoppingcard.util.exception.ItemAlreadyAddedException
import com.timewax.shoppingcard.util.exception.ItemNotFoundException
import com.timewax.shoppingcard.util.exception.ItemSKUMissMatchException
import org.springframework.stereotype.Service

@Service
class ItemManagerService {

    private val items = mutableMapOf<String, Item>()

    fun addItem(item: Item) {
        if (items.containsKey(item.sku)) {
            throw ItemAlreadyAddedException(sku = item.sku)
        }
        items[item.sku] = item
    }

    fun getItem(sku: String): Item? {
        val item = items[sku]
        item?.let {
            return item
        }
        throw ItemNotFoundException(sku = sku)
    }

    fun updateItem(sku: String, newItem: Item) {
        val item = getItem(sku)
        item?.let {
            if (it.sku == newItem.sku) {
                items[sku] = newItem
            } else {
                throw ItemSKUMissMatchException(sku = item.sku, newSku = newItem.sku)
            }
        }
    }

    fun deleteItem(sku: String) {
        val item = getItem(sku)
        item?.let {
            items.remove(sku)
        }
    }
}