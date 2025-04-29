package com.pedrorodrigues.desafiofinal.service.task

import com.pedrorodrigues.desafiofinal.core.exception.NotFoundException
import com.pedrorodrigues.desafiofinal.core.util.genId
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import com.pedrorodrigues.desafiofinal.repository.ProjectCustomTaskRepository
import com.pedrorodrigues.desafiofinal.service.project.ProjectReadService
import com.pedrorodrigues.desafiofinal.service.project.ProjectWriteService
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
    private val projectWriteService: ProjectWriteService,
    private val repository: ProjectCustomTaskRepository,
    private val createValidators: List<TaskCreateValidator>,
    private val updateValidators: List<TaskUpdateValidator>
) : TaskReadService, TaskWriteService {

    override fun findAll(projectId: String): List<Task> {
        if (projectReadService.findById(projectId) == null)
            throw NotFoundException("Nenhuma Task encontrada.")

        return repository.findAllByProjectId(projectId)
    }

    override fun findById(projectId: String, taskId: String): Task? {
        this.findProjectByIdOrThrow(projectId)

        return repository.findByProjectIdAndTaskId(projectId, taskId)
    }

    override fun findByTitle(projectId: String, title: String): Task {
        this.findProjectByIdOrThrow(projectId)

        return repository.findByProjectIdAndTitleContaining(projectId, title).first()
    }

    override fun count(projectId: String): Long {
        this.findProjectByIdOrThrow(projectId)

        return repository.countTasksByProjectId(projectId)
    }

    override fun create(projectId: String, task: Task): Task {
        val project = this.findProjectByIdOrThrow(projectId)
        val newTask = task.copy(
            id = genId(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        createValidators.forEach { it.validate(project, task) }
        projectWriteService.update(projectId, project.copy(tasks = project.tasks?.plus(newTask)))

        return newTask
    }

    override fun update(projectId: String, taskId: String, task: Task): Task {
        val project: Project = this.findProjectByIdOrThrow(projectId)
        val index: Int = project.tasks?.indexOfFirst { it.id == taskId }
            ?: throw NotFoundException("Nenhuma Task encontrada.")
        if (index < 0) throw NotFoundException("Nenhuma Task encontrada.")

        val existing = project.tasks[index]

        updateValidators.forEach { it.validate(project, existing, task) }

        val updatedTask = existing.copy(
            title = task.title,
            status = task.status,
            dueDate = task.dueDate,
            priority = task.priority,
            assignee = task.assignee,
            updatedAt = LocalDateTime.now()
        )

        val newTasks = project.tasks.toMutableList().apply { this[index] = updatedTask }
        projectWriteService.update(projectId, project.copy(tasks = newTasks))

        return updatedTask
    }

    override fun delete(projectId: String, taskId: String) {
        val project = this.findProjectByIdOrThrow(projectId)

        if (project.tasks?.none { it.id == taskId } == true)
            throw NotFoundException("Nenhuma Task encontrada.")

        val filtered = project.tasks?.filterNot { it.id == taskId }

        projectWriteService.update(projectId, project.copy(tasks = filtered))
    }

    private fun findProjectByIdOrThrow(projectId: String): Project =
        projectReadService.findById(projectId)
            ?: throw NotFoundException("O projeto da task busacada não foi encontrado.")
}