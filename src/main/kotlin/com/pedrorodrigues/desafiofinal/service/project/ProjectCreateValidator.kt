package com.pedrorodrigues.desafiofinal.service.project

import com.pedrorodrigues.desafiofinal.core.exception.ValidationException
import com.pedrorodrigues.desafiofinal.core.util.takeExistsAndIf
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.repository.ProjectRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

interface ProjectCreateValidator {
    fun validate(project: Project)
}

@Component
class UniqueProjectNameCreateValidator(
    private val repository: ProjectRepository
) : ProjectCreateValidator {
    override fun validate(project: Project) {
        repository.findByName(project.name)
            .takeExistsAndIf { it.id != project.id }
            ?.let {
                throw ValidationException(
                    "Já existe um projeto com o nome informado (${project.name}), por favor, crie um novo."
                )
            }
    }
}

@Component
class EstimatedEndDateCreateValidator : ProjectCreateValidator {
    override fun validate(project: Project) {
        val start = project.startDate
        val end = project.estimatedEndDate

        if (start != null && end != null && end.isBefore(start)) {
            throw ValidationException(
                "Data estimada de finalização (${end}) deve ser após a data de início (${start})."
            )
        }
    }
}

@Component
class DescriptionLengthCreateValidator(
    @Value("\${app.project.max-description-length:500}")
    private val maxLen: Int
) : ProjectCreateValidator {
    override fun validate(project: Project) {
        project.description
            ?.takeIf { it.length > maxLen }
            ?.let {
                throw ValidationException(
                    "Descrição excede o tamanho máximo de $maxLen caracteres tamanho recebido: ${it.length}."
                )
            }
    }
}

@Component
class TaskLimitCreateValidator(
    @Value("\${app.project.max-tasks:100}")
    private val maxTasks: Int
) : ProjectCreateValidator {
    override fun validate(project: Project) {
        val count = project.tasks.size

        if (count > maxTasks) {
            throw ValidationException(
                "Número de tarefas ($count) excede o limite máximo de $maxTasks."
            )
        }
    }
}