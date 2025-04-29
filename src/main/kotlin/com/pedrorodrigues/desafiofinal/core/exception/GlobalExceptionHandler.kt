package com.pedrorodrigues.desafiofinal.core.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun handleValidation(
        ex: ValidationException,
        request: HttpServletRequest
    ): ResponseEntity<ResponseError> {
        val status = HttpStatus.BAD_REQUEST
        val body = ResponseError(
            status = status.value(),
            error = status.reasonPhrase,
            code = "VALIDATION_ERROR",
            message = ex.message ?: "Erro de validação",
            path = request.requestURI
        )

        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(
        ex: NotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ResponseError> {
        val status = HttpStatus.NOT_FOUND
        val body = ResponseError(
            status = status.value(),
            error = status.reasonPhrase,
            code = "NOT_FOUND",
            message = ex.message ?: "Recurso não encontrado",
            path = request.requestURI
        )

        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ResponseError> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val body = ResponseError(
            status = status.value(),
            error = status.reasonPhrase,
            code = "INTERNAL_SERVER_ERROR",
            message = ex.message ?: "Erro interno no servidor",
            path = request.requestURI
        )

        return ResponseEntity.status(status).body(body)
    }
}
