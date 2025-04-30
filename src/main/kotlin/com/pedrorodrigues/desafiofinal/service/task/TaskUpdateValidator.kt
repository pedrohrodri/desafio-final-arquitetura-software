package com.pedrorodrigues.desafiofinal.service.task

import com.pedrorodrigues.desafiofinal.core.exception.ValidationException
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface TaskUpdateValidator {
    fun validate(project: Project, existingTask: Task, newTask: Task)
}

@Component
class TaskTitleNotBlankUpdateValidator : TaskUpdateValidator {
    override fun validate(project: Project, existingTask: Task, newTask: Task) {
        if (newTask.title.isBlank()) {
            throw ValidationException("O título da Task não pode estar em branco.")
        }
    }
}

@Component
class TaskTitleLengthUpdateValidator(
    @Value("\${app.task.title.min-length:3}") private val minLen: Int,
    @Value("\${app.task.title.max-length:100}") private val maxLen: Int
) : TaskUpdateValidator {
    override fun validate(project: Project, existingTask: Task, newTask: Task) {
        val len = newTask.title.length

        if (len < minLen || len > maxLen) {
            throw ValidationException("O título deve ter entre $minLen e $maxLen caracteres, atualmente tem ${newTask.title.length}.")
        }
    }
}

@Component
class TaskDueDateNotPastUpdateValidator : TaskUpdateValidator {
    override fun validate(project: Project, existingTask: Task, newTask: Task) {
        newTask.dueDate
            ?.takeIf { it.isBefore(LocalDateTime.now()) }
            ?.let {
                throw ValidationException("A data de vencimento (${it}) não pode estar no passado.")
            }
    }
}


@Component
class TaskUniqueTitleUpdateValidator : TaskUpdateValidator {
    override fun validate(project: Project, existingTask: Task, newTask: Task) {
        if (
            newTask.title != existingTask.title &&
            project.tasks.any { it.title.equals(newTask.title, ignoreCase = true) }
        ) {
            throw ValidationException("Já existe outra Task com título '${newTask.title}' neste projeto.")
        }
    }
}