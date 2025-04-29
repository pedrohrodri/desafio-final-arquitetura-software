package com.pedrorodrigues.desafiofinal.dto

import com.pedrorodrigues.desafiofinal.model.task.Task
import java.time.LocalDateTime

data class ProjectDTO(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val estimatedEndDate: String? = null,
    val tasks: List<Task>? = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)