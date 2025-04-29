package com.pedrorodrigues.desafiofinal.core.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ResponseError(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val code: String,
    val message: String,
    val path: String
)