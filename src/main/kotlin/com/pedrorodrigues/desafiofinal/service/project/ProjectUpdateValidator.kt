package com.pedrorodrigues.desafiofinal.service.project

import com.pedrorodrigues.desafiofinal.model.project.Project

interface ProjectUpdateValidator {
  fun validateOnUpdate(existing: Project, updates: Project)
}