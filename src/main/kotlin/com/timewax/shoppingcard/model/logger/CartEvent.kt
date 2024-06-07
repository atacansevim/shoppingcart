package com.timewax.shoppingcard.model.logger

import com.timewax.shoppingcard.util.CartEventEnum
import java.time.ZonedDateTime

data class CartEvent(
    val timestamp: ZonedDateTime,
    val eventType: CartEventEnum,
    val details: String
)