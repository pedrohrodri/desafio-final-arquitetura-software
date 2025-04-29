package com.pedrorodrigues.desafiofinal.mapper

import com.pedrorodrigues.desafiofinal.dto.TaskDTO
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TaskMapper {
    fun toDto(entity: Task): TaskDTO
    fun toEntity(dto: TaskDTO): Task
}