CREATE TABLE chamados (
    id               UUID         NOT NULL,
    protocolo        VARCHAR(20)  NOT NULL,
    titulo           VARCHAR(150) NOT NULL,
    descricao        TEXT         NOT NULL,

    categoria        VARCHAR(30)  NOT NULL,
    status           VARCHAR(30)  NOT NULL DEFAULT 'ABERTO',
    prioridade       VARCHAR(20)  NOT NULL DEFAULT 'MEDIA',
    canal_origem     VARCHAR(20)  NOT NULL DEFAULT 'PORTAL_WEB',
    setor            VARCHAR(30)  NOT NULL,

    empresa_id       UUID         NOT NULL,
    solicitante_id   UUID         NOT NULL,
    responsavel_id   UUID,

    prazo_resolucao  TIMESTAMP(6) NOT NULL,
    data_resolucao   TIMESTAMP(6),
    data_fechamento  TIMESTAMP(6),

    ativo            BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em        TIMESTAMP(6) NOT NULL,
    versao           BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT uk_chamados_protocolo UNIQUE (protocolo),

    CONSTRAINT fk_chamados_empresa     FOREIGN KEY (empresa_id)     REFERENCES empresas (id),
    CONSTRAINT fk_chamados_solicitante FOREIGN KEY (solicitante_id) REFERENCES usuarios (id),
    CONSTRAINT fk_chamados_responsavel FOREIGN KEY (responsavel_id) REFERENCES usuarios (id)
);

CREATE INDEX idx_chamados_empresa_criado_em ON chamados (empresa_id, criado_em);
CREATE INDEX idx_chamados_status_prazo_resolucao ON chamados (status, prazo_resolucao);