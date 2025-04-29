package com.pedrorodrigues.desafiofinal.repository

import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectCustomTaskRepository : MongoRepository<Project, String> {
    @Aggregation(
        pipeline = [
            "{\"\\\$match\": {\"_id\": \"?0\"}}",
            "{\"\\\$unwind\": \"\\\$tasks\"}",
            "{\"\\\$replaceRoot\": {\"newRoot\": \"\\\$tasks\"}}"
        ]
    )
    fun findAllByProjectId(projectId: String): List<Task>

    @Aggregation(
        pipeline = [
            "{\"\\\$match\": {\"_id\": \"?0\"}}",
            "{\"\\\$unwind\": \"\\\$tasks\"}",
            "{\"\\\$match\": {\"tasks.id\": \"?1\"}}",
            "{\"\\\$replaceRoot\": {\"newRoot\": \"\\\$tasks\"}}"
        ]
    )
    fun findByProjectIdAndTaskId(projectId: String, taskId: String): Task?

    @Aggregation(
        pipeline = [
            "{\"\\\$match\": {\"_id\": \"?0\"}}",
            "{\"\\\$unwind\": \"\\\$tasks\"}",
            "{\"\\\$match\": {\"tasks.title\": {\"\\\$regex\": \"?1\", \"\\\$options\": \"i\"}}}",
            "{\"\\\$replaceRoot\": {\"newRoot\": \"\\\$tasks\"}}"
        ]
    )
    fun findByProjectIdAndTitleContaining(projectId: String, title: String): List<Task>

    @Aggregation(
        pipeline = [
            "{\"\\\$match\": {\"_id\": \"?0\"}}",
            "{\"\\\$project\": {\"_id\": 0, \"taskCount\": {\"\\\$size\": \"\\\$tasks\"}}}"
        ]
    )
    fun countTasksByProjectId(projectId: String): Long
}
