package com.pedrorodrigues.desafiofinal.service.project

import com.pedrorodrigues.desafiofinal.core.exception.ValidationException
import com.pedrorodrigues.desafiofinal.core.util.takeExistsAndIf
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.repository.ProjectRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

interface ProjectUpdateValidator {
    fun validateOnUpdate(existing: Project, updates: Project)
}

@Component
class UniqueProjectNameUpdateValidator(
    private val repository: ProjectRepository
) : ProjectUpdateValidator {
    override fun validateOnUpdate(existing: Project, updates: Project) {
        val newName = updates.name
        if (newName != existing.name) {
            repository.findByName(newName)
                .takeExistsAndIf { it.id != existing.id }
                ?.let {
                    throw ValidationException(
                        "Já existe um projeto com o nome informado ($newName)."
                    )
                }
        }
    }
}

@Component
class EstimatedEndDateUpdateValidator : ProjectUpdateValidator {
    override fun validateOnUpdate(existing: Project, updates: Project) {
        val start = updates.startDate ?: existing.startDate
        val end = updates.estimatedEndDate ?: existing.estimatedEndDate

        if (start != null && end != null && end.isBefore(start)) {
            throw ValidationException(
                "Data estimada de finalização ($end) deve ser após a data de início ($start)."
            )
        }
    }
}

@Component
class DescriptionLengthUpdateValidator(
    @Value("\${app.project.max-description-length:500}")
    private val maxLen: Int
) : ProjectUpdateValidator {
    override fun validateOnUpdate(existing: Project, updates: Project) {
        updates.description
            ?.takeIf { it.length > maxLen }
            ?.let {
                throw ValidationException(
                    "Descrição excede o tamanho máximo de $maxLen caracteres tamanho recebido: ${it.length}."
                )
            }
    }
}

@Component
class TaskLimitUpdateValidator(
    @Value("\${app.project.max-tasks:100}")
    private val maxTasks: Int
) : ProjectUpdateValidator {
    override fun validateOnUpdate(existing: Project, updates: Project) {
        val count = updates.tasks?.size ?: existing.tasks?.size ?: 0
        if (count > maxTasks) {
            throw ValidationException(
                "Número de tarefas ($count) excede o limite máximo de $maxTasks."
            )
        }
    }
}
