package com.pedrorodrigues.desafiofinal.model.task

import java.time.LocalDateTime

enum class TaskStatus { OPEN, IN_PROGRESS, DONE }

enum class Priority { LOW, MEDIUM, HIGH }

data class Task(
    val id: String? = null,
    var title: String,
    var status: TaskStatus = TaskStatus.OPEN,
    var dueDate: LocalDateTime? = null,
    var priority: Priority = Priority.MEDIUM,
    var assignee: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)
