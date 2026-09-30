# TaskVault API

API REST de gerenciamento de tarefas construída com **Spring Boot** e **Spring Security**, utilizando **JWT (JSON Web Token)** como mecanismo de autenticação stateless.

## Sobre o projeto

O TaskVault API é um sistema de tarefas onde usuários podem criar, assumir e concluir tarefas, com controle de acesso baseado em papéis (roles) e regras de autorização customizadas (ex: apenas o solicitante de uma tarefa pode atualizá-la, ou um administrador).

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4.1.1**
  - Spring Web (MVC)
  - Spring Data JPA
  - Spring Security (com `@EnableMethodSecurity`)
- **JJWT 0.12.7** — geração e validação de tokens JWT
- **PostgreSQL** — banco de dados relacional
- **MapStruct** — mapeamento entre entidades e DTOs
- **Lombok** — redução de boilerplate
- **SpringDoc OpenAPI (Swagger UI)** — documentação interativa dos endpoints
- **Maven** (com Maven Wrapper)

## Arquitetura

```
src/main/java/dev/davisantos/TaskVaultApi
├── config          # Configurações de segurança, filtro JWT e provider de token
├── controller       # Endpoints REST
├── database
│   ├── model        # Entidades JPA
│   └── repository   # Repositórios Spring Data JPA
├── dto              # Records usados como request/response
├── exception        # Exceções customizadas
├── mapper           # Interfaces MapStruct
├── service          # Regras de negócio e autorização
└── utils            # Utilitários compartilhados entre controllers
```

## Autenticação e autorização

- Ao efetuar login (`POST /auth/login`), a API retorna um token JWT assinado (HMAC-SHA) com tempo de expiração configurável.
- O token deve ser enviado no header `Authorization: Bearer <token>` nas requisições subsequentes.
- Um filtro (`JwtAuthenticationFilter`) intercepta cada requisição, valida o token e popula o contexto de segurança do Spring.
- Além da autenticação, o projeto usa `@PreAuthorize` para controle de acesso baseado em papéis (`ROLE_ADMIN`, `ROLE_TECHNICIAN`, `ROLE_USER`) e em regras customizadas de autorização (ex: `AuthorizationService`, que valida se o usuário autenticado é o solicitante da tarefa antes de permitir uma atualização).

> **Nota:** este é um projeto de estudo. A chave de assinatura do JWT possui um valor padrão hardcoded em `application.yaml` apenas para facilitar a execução local, mas pode (e deve) ser sobrescrita pela variável de ambiente `JWT_KEY`. Em um ambiente de produção, o correto seria usar uma chave forte, gerada aleatoriamente, fornecida somente via variável de ambiente/secret manager, sem valor padrão no código.

## Como executar o projeto

### Pré-requisitos

- JDK 21+
- PostgreSQL em execução (local ou em container)

### 1. Banco de dados

Crie um banco de dados PostgreSQL chamado `task_vault_db` (ou ajuste a URL de conexão conforme necessário):

```sql
CREATE DATABASE task_vault_db;
```

### 2. Variáveis de ambiente (opcional)

| Variável         | Descrição                                   | Padrão                          |
|------------------|----------------------------------------------|----------------------------------|
| `JWT_KEY`        | Chave secreta usada para assinar os tokens    | valor de estudo hardcoded        |
| `JWT_EXPIRATION` | Tempo de expiração do token, em ms            | `900000` (15 minutos)            |
| `ADMIN_NAME`     | Nome do usuário administrador criado no seed  | `dev_admin`                       |
| `ADMIN_USERNAME` | Username do usuário administrador             | `admin`                          |
| `ADMIN_PASSWORD` | Senha do usuário administrador                | `admin`                          |

As credenciais do banco de dados (`spring.datasource.*`) estão fixas em `application.yaml` para facilitar a execução local (`postgres`/`postgres`).

### 3. Executando a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em `http://localhost:8080`.

Ao iniciar, um `CommandLineRunner` popula automaticamente os papéis (`ROLE_ADMIN`, `ROLE_TECHNICIAN`, `ROLE_USER`) e cria um usuário administrador padrão, caso ainda não existam.

### 4. Documentação da API

Com a aplicação em execução, a documentação interativa (Swagger UI) fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints principais

### Autenticação (`/auth`) — acesso público

| Método | Endpoint         | Descrição                          |
|--------|------------------|--------------------------------------|
| POST   | `/auth/login`    | Autentica um usuário e retorna o JWT |
| POST   | `/auth/register` | Registra um novo usuário             |

### Usuários (`/users`) — requer autenticação

| Método | Endpoint      | Descrição                              | Restrição       |
|--------|---------------|-------------------------------------------|------------------|
| GET    | `/users`      | Lista todos os usuários                    | Autenticado      |
| GET    | `/users/{id}` | Busca um usuário por ID                    | Autenticado      |
| PUT    | `/users/{id}` | Atualiza um usuário                        | Autenticado      |
| DELETE | `/users/{id}` | Remove um usuário                          | `ROLE_ADMIN`     |

### Tarefas (`/tasks`) — requer autenticação

| Método | Endpoint                | Descrição                                             | Restrição                                         |
|--------|--------------------------|--------------------------------------------------------|-----------------------------------------------------|
| GET    | `/tasks`                 | Lista todas as tarefas                                  | Autenticado                                          |
| GET    | `/tasks/{id}`            | Busca uma tarefa por ID                                 | Autenticado                                          |
| POST   | `/tasks`                 | Cria uma nova tarefa                                    | Autenticado                                          |
| PUT    | `/tasks/{id}`            | Atualiza uma tarefa                                     | `ROLE_ADMIN` ou solicitante da tarefa               |
| DELETE | `/tasks/{id}`            | Remove uma tarefa                                       | `ROLE_ADMIN`                                         |
| POST   | `/tasks/{id}/assign`     | Atribui a tarefa a um usuário (owner)                   | `ROLE_ADMIN`                                         |
| POST   | `/tasks/{id}/take`       | O usuário autenticado assume a tarefa para si            | Qualquer papel, exceto `ROLE_TECHNICIAN`             |
| POST   | `/tasks/{id}/complete`   | Marca a tarefa como concluída                           | `ROLE_ADMIN`, `ROLE_TECHNICIAN` (dono da tarefa)     |

## Licença

Este projeto está licenciado sob os termos da licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.