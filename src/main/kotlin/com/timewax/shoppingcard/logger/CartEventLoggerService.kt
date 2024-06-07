package com.timewax.shoppingcard.logger

import com.timewax.shoppingcard.model.logger.CartEvent
import com.timewax.shoppingcard.util.CartEventEnum
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class CartEventLoggerService {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun logEvent(eventType: CartEventEnum, details: String) {
        val event = CartEvent(
            timestamp = ZonedDateTime.now(),
            eventType = eventType,
            details = details
        )
        log.info("Cart Event: ${event.eventType}, Details: $details")
    }
}