package com.timewax.shoppingcard.interceptor

import com.timewax.shoppingcard.util.exception.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalHttpInterceptor {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        log.error(ex.toString())
        val result = ex.bindingResult
        val errors = result.fieldErrors
        var message = ""
        for (error in errors) {
            message += "${error.defaultMessage}\n"
        }
        return ResponseEntity.badRequest().body(message)
    }

    @ExceptionHandler(ItemAlreadyAddedException::class)
    fun handleItemAlreadyInCartException(ex: ItemAlreadyAddedException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFoundException(ex: ItemNotFoundException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(ItemNotFoundInCartException::class)
    fun handleItemNotFoundInCartException(ex: ItemNotFoundInCartException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(CartAlreadyEmptyException::class)
    fun handleCartAlreadyEmptyException(ex: CartAlreadyEmptyException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(ItemAlreadyInCartException::class)
    fun handleItemAlreadyInCartExceptionException(ex: ItemAlreadyInCartException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(InvalidDiscountCodeException::class)
    fun handleItemAlreadyInCartExceptionException(ex: InvalidDiscountCodeException): ResponseEntity<String> {
        log.error(ex.toString())
        return ResponseEntity.badRequest().body(ex.message)
    }
}