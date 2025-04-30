package com.pedrorodrigues.desafiofinal.model.project

import com.pedrorodrigues.desafiofinal.core.util.genId
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "projects")
data class Project(
    val id: String? = genId(),
    var name: String,
    var description: String? = null,
    var startDate: LocalDateTime? = null,
    var estimatedEndDate: LocalDateTime? = null,
    val tasks: List<Task>? = emptyList(),
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val updatedAt: LocalDateTime? = null
)