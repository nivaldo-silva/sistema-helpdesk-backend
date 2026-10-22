CREATE TABLE usuarios (
    id            UUID         NOT NULL,
    nome          VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    senha         VARCHAR(255) NOT NULL,
    telefone      VARCHAR(20),
    perfil        VARCHAR(20)  NOT NULL,
    empresa_id    UUID,
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    ultimo_acesso TIMESTAMP(6),
    criado_em     TIMESTAMP(6) NOT NULL,
    versao        BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT fk_usuarios_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT ck_usuarios_cliente_exige_empresa CHECK (perfil <> 'CLIENTE' OR empresa_id IS NOT NULL)
);

CREATE INDEX idx_usuarios_empresa ON usuarios (empresa_id);