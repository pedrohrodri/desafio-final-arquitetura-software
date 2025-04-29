## Tecnologias

- Kotlin 1.9 + Spring Boot 3
- Spring Data MongoDB
- MapStruct
- Docker / Docker Compose
- Gradle Kotlin DSL
- draw.io (para diagramas C4)

---

## 🔗 Links de Demo

- **Swagger UI**  
  https://desafio-final-api-da45594113cf.herokuapp.com/swagger-ui/index.html

- **OpenAPI JSON (`/v3/api-docs`)**  
  https://desafio-final-api-da45594113cf.herokuapp.com/v3/api-docs

---

## Como Executar

1. Clone este repositório:

   ```bash
   git clone https://github.com/pedrohrodri/desafio-final-arquitetura-software.git &&
   cd desafio-final-arquitetura-software
   ```
   
2. Suba os containers com Docker Compose:

   ```bash
   docker-compose up --build
   ```
   
   – Isso irá:
    - Baixar a imagem do MongoDB e subir o container.
    - Gerar a imagem da sua API (via Dockerfile) e executá-la.

3. Acesse a API em http://localhost:8080

Se quiser subir só a API (supondo que já tenha um MongoDB local rodando), use:

```bash
./gradlew bootRun
```

ou

```bash
docker build -t desafio-final .
docker run -p 8080:8080 desafio-final
```

---

## Estrutura de Pastas

```bash
src/
└── main/
    ├── kotlin/
    │   └── com/pedrorodrigues/desafiofinal/
    │       ├── core/                       # Configurações gerais, exceções e utilitários
    │       ├── controller/                 # Controllers REST (endpoints HTTP)
    │       ├── dto/                        # Data Transfer Objects (entrada/saída da API)
    │       ├── mapper/                     # MapStruct mappers (Model ↔ DTO)
    │       ├── model/                      # Entidades de domínio (@Document)
    │       ├── repository/                 # Repositórios Spring Data MongoDB
    │       ├── service/                    # Serviços de leitura e escrita (business logic)
    │       └── DesafioFinalApplication.kt  # Ponto de entrada do Spring Boot
    └── resources/
        └── application.properties         # Configurações (MongoDB, porta, etc.)
Dockerfile                                  # Imagem da aplicação
docker-compose.yml                          # Compose para MongoDB + API
build.gradle.kts                            # Script de build e dependências
```

### Descrição dos Componentes

- **core/**  
  Contém configurações globais, classes de exceção (ex.: `ValidationException`), beans e utilitários compartilhados.

- **controller/**  
  Classes anotadas com `@RestController` que expõem os endpoints da API:
    - `ProjectController` → CRUD de projetos
    - `TaskController`   → CRUD de tarefas associadas a um projeto

- **dto/**  
  Objetos simples para transporte de dados entre cliente e servidor, desacoplando o modelo de domínio.

- **mapper/**  
  Interfaces MapStruct (`ProjectMapper`, `TaskMapper`) que convertem entre entidades de domínio e DTOs.

- **model/**  
  Entidades de domínio anotadas com `@Document` para persistência no MongoDB:
    - `Project`
    - `Task`

- **repository/**  
  Extensões de `MongoRepository` para acesso a dados no MongoDB:
    - `ProjectRepository`
    - `TaskRepository`

- **service/**  
  Definição de interfaces de leitura/escrita e implementações, incluindo validações de negócio:
    - `ProjectReadService` / `ProjectWriteService`
    - `ProjectServiceImpl` (com `ProjectCreateValidator` e `ProjectUpdateValidator`)
    - `TaskReadService` / `TaskWriteService`
    - `TaskServiceImpl`

- **DesafioFinalApplication.kt**  
  Classe principal que inicia o Spring Boot.

- **resources/application.properties**  
  Configurações de conexão com o MongoDB, porta do servidor e parâmetros de aplicação.

- **Dockerfile**  
  Instruções para criar a imagem Docker da API.

- **docker-compose.yml**  
  Orquestração de contêiner para subir a API e uma instância de MongoDB.

- **build.gradle.kts**  
  Script de build Kotlin DSL com plugins, dependências e toolchain configurados.