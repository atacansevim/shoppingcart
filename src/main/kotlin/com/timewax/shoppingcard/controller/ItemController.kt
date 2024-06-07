package com.timewax.shoppingcard.controller

import com.timewax.shoppingcard.model.Item
import com.timewax.shoppingcard.service.ItemManagerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/items")
class ItemController (
    private val itemManagerService: ItemManagerService
) {

    @PostMapping("/add")
    fun addItem(@Valid @RequestBody item: Item): ResponseEntity<String> {
        itemManagerService.addItem(item)
        return ResponseEntity.status(HttpStatus.CREATED).body("Item added successfully")
    }

    @GetMapping("/{sku}")
    fun getItem(@PathVariable sku: String): ResponseEntity<Item> {
        val item = itemManagerService.getItem(sku)
        return ResponseEntity.status(HttpStatus.OK).body(item)
    }

    @PutMapping("/{sku}")
    fun updateItem(@PathVariable sku: String,@Valid @RequestBody newItem: Item): ResponseEntity<String> {
        itemManagerService.updateItem(sku, newItem)
        return ResponseEntity.ok("Item updated successfully")
    }

    @DeleteMapping("/{sku}")
    fun deleteItem(@PathVariable sku: String): ResponseEntity<String> {
        itemManagerService.deleteItem(sku)
        return ResponseEntity.ok("Item deleted successfully")
    }
}