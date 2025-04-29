package com.pedrorodrigues.desafiofinal.repository

import com.pedrorodrigues.desafiofinal.model.project.Project
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProjectRepository : MongoRepository<Project, String> {
    fun findByName(name: String): Optional<Project>
}