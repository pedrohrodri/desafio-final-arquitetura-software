package com.pedrorodrigues.desafiofinal.service.task

import com.pedrorodrigues.desafiofinal.core.exception.NotFoundException
import com.pedrorodrigues.desafiofinal.core.util.genId
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import com.pedrorodrigues.desafiofinal.repository.ProjectCustomReadTaskRepository
import com.pedrorodrigues.desafiofinal.repository.ProjectCustomWriteTaskRepository
import com.pedrorodrigues.desafiofinal.service.project.ProjectReadService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface TaskReadService {
    fun findAll(projectId: String): List<Task>
    fun findById(projectId: String, taskId: String): Task?
    fun findByTitle(projectId: String, title: String): Task
    fun count(projectId: String): Long
}

interface TaskWriteService {
    fun create(projectId: String, task: Task): Task
    fun update(projectId: String, taskId: String, task: Task): Task
    fun delete(projectId: String, taskId: String)
}

@Service
class TaskServiceImpl(
    private val projectReadService: ProjectReadService,
    private val repositoryWrite: ProjectCustomWriteTaskRepository,
    private val repositoryRead: ProjectCustomReadTaskRepository,
    private val createValidators: List<TaskCreateValidator>,
    private val updateValidators: List<TaskUpdateValidator>
) : TaskReadService, TaskWriteService {

    override fun findAll(projectId: String): List<Task> {
        if (projectReadService.findById(projectId) == null)
            throw NotFoundException("Nenhuma Task encontrada.")

        return repositoryRead.findAllByProjectId(projectId)
    }

    override fun findById(projectId: String, taskId: String): Task? {
        this.findProjectByIdOrThrow(projectId)

        return repositoryRead.findByProjectIdAndTaskId(projectId, taskId)
    }

    override fun findByTitle(projectId: String, title: String): Task {
        this.findProjectByIdOrThrow(projectId)

        return repositoryRead.findByProjectIdAndTitleContaining(projectId, title).first()
    }

    override fun count(projectId: String): Long {
        this.findProjectByIdOrThrow(projectId)

        return repositoryRead.countTasksByProjectId(projectId)
    }

    override fun create(projectId: String, task: Task): Task {
        val project = findProjectByIdOrThrow(projectId)
        val newTask = task.copy(
            id = genId(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        createValidators.forEach { it.validate(project, newTask) }
        repositoryWrite.addTask(projectId, newTask)

        return newTask
    }

    override fun update(projectId: String, taskId: String, task: Task): Task {
        val project = findProjectByIdOrThrow(projectId)
        val existing = project.tasks!!.firstOrNull { it.id == taskId }
            ?: throw NotFoundException("Nenhuma Task encontrada.")
        updateValidators.forEach { it.validate(project, existing, task) }

        val updated = existing.copy(
            title = task.title,
            status = task.status,
            dueDate = task.dueDate,
            priority = task.priority,
            assignee = task.assignee,
            updatedAt = LocalDateTime.now()
        )
        repositoryWrite.updateTask(projectId, taskId, updated)
        return updated
    }

    override fun delete(projectId: String, taskId: String) {
        findProjectByIdOrThrow(projectId)
        repositoryWrite.removeTask(projectId, taskId)
    }

    private fun findProjectByIdOrThrow(projectId: String): Project =
        projectReadService.findById(projectId)
            ?: throw NotFoundException("O projeto da task busacada não foi encontrado.")
}