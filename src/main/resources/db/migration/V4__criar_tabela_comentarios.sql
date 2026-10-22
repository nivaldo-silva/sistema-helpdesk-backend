CREATE TABLE comentarios (
    id            UUID         NOT NULL,
    mensagem      TEXT         NOT NULL,
    nota_interna  BOOLEAN      NOT NULL DEFAULT FALSE,
    chamado_id    UUID         NOT NULL,
    autor_id      UUID         NOT NULL,
    criado_em     TIMESTAMP(6) NOT NULL,
    versao           BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),

    CONSTRAINT fk_comentarios_chamado FOREIGN KEY (chamado_id) REFERENCES chamados (id) ON DELETE CASCADE,
    CONSTRAINT fk_comentarios_autor   FOREIGN KEY (autor_id)   REFERENCES usuarios (id)
);

CREATE INDEX idx_comentarios_chamado_criado_em ON comentarios (chamado_id, criado_em);