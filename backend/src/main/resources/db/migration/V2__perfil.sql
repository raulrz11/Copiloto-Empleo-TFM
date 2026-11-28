CREATE TABLE perfil (
    id              BIGSERIAL PRIMARY KEY,
    usuario_id      BIGINT       NOT NULL UNIQUE REFERENCES usuario (id) ON DELETE CASCADE,
    titulo         VARCHAR(150),
    resumen         TEXT,
    cv_texto        TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE experiencia (
    id            BIGSERIAL PRIMARY KEY,
    perfil_id     BIGINT       NOT NULL REFERENCES perfil (id) ON DELETE CASCADE,
    tipo          VARCHAR(20)  NOT NULL,
    titulo        VARCHAR(150) NOT NULL,
    organizacion  VARCHAR(150),
    url           VARCHAR(500),
    fecha_inicio  DATE,
    fecha_fin     DATE,
    descripcion   TEXT,
    habilidades   JSONB        NOT NULL DEFAULT '[]',
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
    CONSTRAINT chk_experiencia_tipo CHECK (tipo IN ('LABORAL', 'PROYECTO', 'FORMACION', 'OTRO'))
    CONSTRAINT chk_experiencia_fechas CHECK (fecha_fin IS NULL OR fecha_inicio IS NULL OR fecha_fin >= fecha_inicio)
);

CREATE INDEX idx_experiencia_perfil ON experiencia (perfil_id);

CREATE TABLE HABILIDAD (
    id            BIGSERIAL PRIMARY KEY,
    perfil_id     BIGINT       NOT NULL REFERENCES perfil (id) ON DELETE CASCADE,
    nombre        VARCHAR(100) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT unique_habilidad_perfil_nombre UNIQUE (perfil_id, nombre)
);