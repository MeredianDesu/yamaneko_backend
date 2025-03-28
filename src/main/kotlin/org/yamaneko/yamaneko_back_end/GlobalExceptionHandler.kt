package org.yamaneko.yamaneko_back_end

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.yamaneko.yamaneko_back_end.dto.ErrorResponse
import software.amazon.awssdk.core.exception.SdkClientException

@ControllerAdvice
class GlobalExceptionHandler {
  
  // Handle JSON request
  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleJsonParseException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
    val errorResponse = ErrorResponse(
      error = "Invalid JSON", message = mapOf("details" to "One or more required fields are missing or incorrect")
    )
    
    return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
  }
  
  // Handle fields: validation
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
    val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
    
    val errorResponse = ErrorResponse(
      error = "Validation Failed", message = errors
    )
    
    return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
  }
  
  // Handle error: NOT FOUND
  @ExceptionHandler(NoSuchElementException::class)
  fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
    val errors = ex.message
    
    val errorResponse = ErrorResponse(
      error = "No such element found", message = mapOf("details" to errors)
    )
    
    return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
  }
  
  // AWS SDK Exception handler
  @ExceptionHandler(SdkClientException::class)
  fun handleSdkClientException(ex: SdkClientException): ResponseEntity<ErrorResponse> {
    val errors = ex.message
    val errorResponse = ErrorResponse(
      error = "AWS SDK Client Error", message = mapOf("details" to errors)
    )
    
    return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
  }
  
  // IllegalException handler
  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
    val errors = ex.message
    val errorResponse = ErrorResponse(
      error = "IllegalArgumentException", message = mapOf("details" to errors)
    )
    
    return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
  }
  
  // RuntimeException handler
  @ExceptionHandler(RuntimeException::class)
  fun runtimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
    val errors = ex.message
    val errorResponse = ErrorResponse(
      error = "RuntimeException", message = mapOf("details" to errors)
    )
    
    return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
  }
}