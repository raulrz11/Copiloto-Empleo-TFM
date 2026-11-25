# Copiloto de búsqueda de empleo

![CI](https://github.com/raulrz11/Copiloto-Empleo-TFM/actions/workflows/ci.yml/badge.svg)

Aplicación web que analiza ofertas de empleo frente a tu CV, lleva el seguimiento
de tus candidaturas y te prepara para la entrevista técnica.

Proyecto de fin de máster (desarrollo + IA). En construcción.

## Stack
Java 21 · Spring Boot 4 · Spring AI · Spring Security (JWT) · PostgreSQL + pgvector ·
Flyway · Docker · Angular · GitHub Actions

## Requisitos
- Java 21
- Docker Desktop
- Ollama con un modelo descargado (ver `application.yaml`)

## Arrancar en local
```bash
cp .env.example .env          # y rellena los valores
docker compose up -d          # base de datos
cd backend
./gradlew bootRun             # API en http://localhost:8080
```

## Tests
```bash
cd backend
./gradlew test                # necesita Docker (Testcontainers)
```