package com.pedrorodrigues.desafiofinal.mapper

import com.pedrorodrigues.desafiofinal.dto.ProjectDTO
import com.pedrorodrigues.desafiofinal.model.project.Project
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [TaskMapper::class])
interface ProjectMapper {
    fun toDto(entity: Project): ProjectDTO
    fun toEntity(dto: ProjectDTO): Project

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    fun toEntityForCreate(dto: ProjectDTO): Project
}