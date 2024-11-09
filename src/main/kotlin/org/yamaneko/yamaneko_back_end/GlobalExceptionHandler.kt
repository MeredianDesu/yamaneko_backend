package org.yamaneko.yamaneko_back_end

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.yamaneko.yamaneko_back_end.dto.ErrorResponse

@ControllerAdvice
class GlobalExceptionHandler {

    // Handle JSON request
    @ExceptionHandler( HttpMessageNotReadableException::class )
    @ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
    fun handleJsonParseException( ex: HttpMessageNotReadableException ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            error = "Invalid JSON",
            message = mapOf( "details" to "One or more required fields are missing or incorrect" )
        )
        println( ex.message )

        return ResponseEntity( errorResponse, HttpStatus.UNPROCESSABLE_ENTITY )
    }

    // Handle fields: validation
    @ExceptionHandler( MethodArgumentNotValidException::class )
    @ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
    fun handleValidationException( ex: MethodArgumentNotValidException ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to it.defaultMessage }

        val errorResponse = ErrorResponse(
            error = "Validation Failed",
            message = errors
        )

        return ResponseEntity( errorResponse, HttpStatus.UNPROCESSABLE_ENTITY )
    }
}