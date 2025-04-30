package com.pedrorodrigues.desafiofinal.repository

import com.pedrorodrigues.desafiofinal.model.project.Project
import com.pedrorodrigues.desafiofinal.model.task.Task
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

interface ProjectCustomWriteTaskRepository {
    fun addTask(projectId: String, task: Task)
    fun updateTask(projectId: String, taskId: String, updated: Task)
    fun removeTask(projectId: String, taskId: String)
}

@Repository
class ProjectTaskRepositoryImpl(
    private val mongo: MongoTemplate
) : ProjectCustomWriteTaskRepository {

    override fun addTask(projectId: String, task: Task) {
        val q = Query(Criteria.where("_id").`is`(projectId))
        val u = Update().push("tasks", task)
        mongo.updateFirst(q, u, Project::class.java)
    }

    override fun updateTask(projectId: String, taskId: String, updated: Task) {
        val q = Query(
            Criteria.where("_id").`is`(projectId)
                .and("tasks.id").`is`(taskId)
        )
        val u = Update()
            .set("tasks.$.title", updated.title)
            .set("tasks.$.status", updated.status)
            .set("tasks.$.dueDate", updated.dueDate)
            .set("tasks.$.priority", updated.priority)
            .set("tasks.$.assignee", updated.assignee)
            .set("tasks.$.updatedAt", updated.updatedAt)
        mongo.updateFirst(q, u, Project::class.java)
    }

    override fun removeTask(projectId: String, taskId: String) {
        val q = Query(Criteria.where("_id").`is`(projectId))
        val u = Update().pull("tasks", Query(Criteria.where("id").`is`(taskId)))
        mongo.updateFirst(q, u, Project::class.java)
    }
}
