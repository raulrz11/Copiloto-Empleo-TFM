-- Extensión para guardar embeddings (vectores) en PostgreSQL
-- CREATE EXTENSION vector activa pgvector en esta base de datos. La imagen lo trae instalado, pero hay que activarlo.
-- mientras no tengas datos reales, puedes seguir editando este V1 y recrear la base de datos. Cuando empieces a usarlo de verdad, V1 se congela y los cambios irán en V2, V3…

CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE usuario (
    id             BIGSERIAL PRIMARY KEY,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    nombre         VARCHAR(100),
    creado_en      TIMESTAMPTZ  NOT NULL DEFAULT now()
);