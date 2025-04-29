package com.pedrorodrigues.desafiofinal.controller

import com.pedrorodrigues.desafiofinal.dto.ProjectDTO
import com.pedrorodrigues.desafiofinal.mapper.ProjectMapper
import com.pedrorodrigues.desafiofinal.service.project.ProjectReadService
import com.pedrorodrigues.desafiofinal.service.project.ProjectWriteService
import org.springframework.web.bind.annotation.*

@RequestMapping("/projects")
@RestController
class ProjectController(
    val mapper: ProjectMapper,
    val readService: ProjectReadService,
    val writeService: ProjectWriteService
) {

    @GetMapping
    fun findAll(): List<ProjectDTO> =
        readService.findAll().map { mapper.toDto(it) }

    @GetMapping("/byId/{id}")
    fun findById(@PathVariable id: String): ProjectDTO? =
        readService.findById(id)?.let { mapper.toDto(it) }

    @GetMapping("/byName/{name}")
    fun findByName(@PathVariable name: String): ProjectDTO? =
        readService.findByName(name)?.let { mapper.toDto(it) }

    @GetMapping("/count")
    fun count(): Long = readService.count()

    @PostMapping
    fun create(@RequestBody dto: ProjectDTO): ProjectDTO =
        writeService.create(mapper.toEntityForCreate(dto)).let { mapper.toDto(it) }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody dto: ProjectDTO): ProjectDTO =
        writeService.update(id, mapper.toEntity(dto)).let { mapper.toDto(it) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = writeService.delete(id)

}