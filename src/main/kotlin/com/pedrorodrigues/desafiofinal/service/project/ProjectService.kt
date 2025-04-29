package com.pedrorodrigues.desafiofinal.service.project

import com.pedrorodrigues.desafiofinal.core.exception.NotFoundException
import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.repository.ProjectRepository
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service

interface ProjectReadService {
    fun findAll(): List<Project>
    fun findById(id: String): Project?
    fun findByName(name: String): Project?
    fun count(): Long
}

interface ProjectWriteService {
    fun create(project: Project): Project
    fun update(id: String, project: Project): Project
    fun delete(id: String)
}

@Service
class ProjectServiceImpl(
    private val repository: ProjectRepository,
    private val createValidators: List<ProjectCreateValidator>,
    private val updateValidators: List<ProjectUpdateValidator>
) : ProjectReadService, ProjectWriteService {

    override fun findAll(): List<Project> = repository.findAll()

    override fun findById(id: String): Project? =
        repository.findById(id).orElse(null)

    override fun findByName(name: String): Project? =
        repository.findByName(name).orElse(null)

    override fun count(): Long = repository.count()

    override fun create(project: Project): Project {
        createValidators.forEach { it.validate(project) }

        return repository.save(project)
    }

    override fun update(id: String, project: Project): Project {
        val existingProject = this.findById(id)
            ?: throw NotFoundException("O projeto buscado não foi encontrado.")

        updateValidators.forEach {
            it.validateOnUpdate(
                existingProject,
                project
            )
        }
        return repository.save(project)
    }

    override fun delete(id: String) = repository.deleteById(id)

}