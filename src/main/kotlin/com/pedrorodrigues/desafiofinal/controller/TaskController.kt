package com.pedrorodrigues.desafiofinal.controller

import com.pedrorodrigues.desafiofinal.dto.TaskDTO
import com.pedrorodrigues.desafiofinal.mapper.TaskMapper
import com.pedrorodrigues.desafiofinal.service.task.TaskReadService
import com.pedrorodrigues.desafiofinal.service.task.TaskWriteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks/{projectId}")
class TaskController(
    val mapper: TaskMapper,
    val readService: TaskReadService,
    val writeService: TaskWriteService
) {

    @GetMapping
    fun findAll(@PathVariable projectId: String): List<TaskDTO> =
        readService.findAll(projectId).map { mapper.toDto(it) }

    @GetMapping("/byId/{id}")
    fun findById(
        @PathVariable projectId: String,
        @PathVariable id: String
    ): TaskDTO? = readService.findById(projectId, id)?.let { mapper.toDto(it) }

    @GetMapping("/byName/{title}")
    fun findByName(
        @PathVariable projectId: String,
        @PathVariable title: String
    ): TaskDTO? = readService.findByTitle(projectId, title).let { mapper.toDto(it) }

    @GetMapping("/count")
    fun count(@PathVariable projectId: String): Long = readService.count(projectId)

    @PostMapping
    fun create(
        @PathVariable projectId: String,
        @RequestBody dto: TaskDTO
    ): TaskDTO = writeService.create(projectId, mapper.toEntity(dto)).let { mapper.toDto(it) }

    @PutMapping("/{id}")
    fun update(
        @PathVariable projectId: String,
        @PathVariable id: String,
        @RequestBody dto: TaskDTO
    ): TaskDTO =
        writeService.update(projectId, id, mapper.toEntity(dto)).let { mapper.toDto(it) }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable projectId: String,
        @PathVariable id: String
    ) = writeService.delete(projectId, id)

}