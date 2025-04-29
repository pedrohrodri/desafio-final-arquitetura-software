package com.pedrorodrigues.desafiofinal.service.task

import com.pedrorodrigues.desafiofinal.core.exception.ValidationException
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime

interface TaskCreateValidator {
    fun validate(project: Project, task: Task)
}

@Component
class TaskTitleNotBlankCreateValidator : TaskCreateValidator {
    override fun validate(project: Project, task: Task) {
        if (task.title.isBlank()) {
            throw ValidationException("O título da Task não pode estar em branco.")
        }
    }
}

@Component
class TaskTitleLengthCreateValidator(
    @Value("\${app.task.title.min-length:3}") private val minLen: Int,
    @Value("\${app.task.title.max-length:100}") private val maxLen: Int
) : TaskCreateValidator {
    override fun validate(project: Project, task: Task) {
        val len = task.title.length
        if (len < minLen || len > maxLen) {
            throw ValidationException("O título deve ter entre $minLen e $maxLen caracteres (atual=$len).")
        }
    }
}

@Component
class TaskDueDateNotPastCreateValidator : TaskCreateValidator {
    override fun validate(project: Project, task: Task) {
        task.dueDate
            ?.takeIf { it.isBefore(LocalDateTime.now()) }
            ?.let {
                throw ValidationException("A data de vencimento (${it}) não pode estar no passado.")
            }
    }
}

@Component
class TaskUniqueTitleCreateValidator : TaskCreateValidator {
    override fun validate(project: Project, task: Task) {
        if (project.tasks?.any { it.title.equals(task.title, ignoreCase = true) } == true) {
            throw ValidationException("Já existe uma Task com título '${task.title}' neste projeto.")
        }
    }
}