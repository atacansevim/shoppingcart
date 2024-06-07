package com.timewax.shoppingcard.util.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

open class ItemManagerException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ItemAlreadyAddedException(sku: String) : ItemManagerException("Item with SKU $sku is already added", )

@ResponseStatus(HttpStatus.NOT_FOUND)
class ItemNotFoundException(sku: String) : ItemManagerException("Item with SKU $sku not found")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ItemSKUMissMatchException(sku: String, newSku: String): ItemManagerException("Existing item's SKU $sku does not match with the new SKU $newSku")