# AI Chat Server

A Spring Boot chat application with a browser UI, PostgreSQL, and Ollama. Docker Compose runs the complete stack so Windows, macOS, and Linux developers use the same database, model server, and ports.

## Quick start (recommended)

Install Docker Desktop (Windows/macOS) or Docker Engine with Compose (Linux), then run from the repository root:

```bash
docker compose up --build
```

The first start downloads the Ollama model and can take several minutes. Open:

- UI: http://localhost:8080/
- Ollama: http://localhost:11434/
- PostgreSQL: `localhost:5432`

Compose waits for PostgreSQL and Ollama model installation before starting the application. Database and model data are persisted in named Docker volumes. On this branch, Hibernate's existing `ddl-auto=update` setting manages the development schema.

Stop the stack with:

```bash
docker compose down
```

To reset PostgreSQL and Ollama data (destructive):

```bash
docker compose down -v
```

## Configuration

Defaults work without a `.env` file. To customize ports, credentials, model, or the system prompt, copy `.env.example` to `.env` in the repository root. On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

On macOS/Linux use `cp .env.example .env`. Edit `.env`, then recreate the stack with `docker compose up --build`.

Useful settings:

| Variable | Default | Purpose |
|---|---|---|
| `APP_PORT` | `8080` | Host port for the browser UI |
| `POSTGRES_PORT` | `5432` | Host port for PostgreSQL |
| `OLLAMA_PORT` | `11434` | Host port for Ollama |
| `OLLAMA_MODEL` | `llama3.2:3b` | Model downloaded at startup |
| `POSTGRES_PASSWORD` | local-only default | Change for shared/non-local deployments |
| `AI_SYSTEM_PROMPT` | empty | Optional system prompt |

The Compose setup uses CPU by default and therefore works without a GPU. Nvidia GPU acceleration can be added with a Docker Compose override and a compatible GPU-enabled Docker installation.

## Development without Docker

Requirements: Java 17, Maven-compatible network access, PostgreSQL, and Ollama. Configure the application with environment variables rather than editing committed files:

```bash
DB_URL=jdbc:postgresql://localhost:5432/ai_chat
DB_USERNAME=ai_chat_user
DB_PASSWORD=your-password
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL=llama3.2:3b
```

Then run:

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`. Ensure PostgreSQL and Ollama are already running.

## Verify the setup

```bash
docker compose ps
docker compose logs app
docker compose exec ollama ollama list
```

## API/UI features

The UI supports registering/login, listing, creating, deleting, and viewing conversations, and sending messages to the configured Ollama model. The backend and zero-build frontend are both served at `http://localhost:8080/`.
