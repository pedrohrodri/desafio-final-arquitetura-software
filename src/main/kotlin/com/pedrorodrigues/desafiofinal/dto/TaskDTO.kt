package com.pedrorodrigues.desafiofinal.dto

import com.pedrorodrigues.desafiofinal.core.util.genId
import com.pedrorodrigues.desafiofinal.model.task.Priority
import com.pedrorodrigues.desafiofinal.model.task.TaskStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

data class TaskDTO(
    val id: String? = genId(),
    val title: String,
    val status: TaskStatus = TaskStatus.OPEN,
    val dueDate: LocalDateTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val assignee: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
