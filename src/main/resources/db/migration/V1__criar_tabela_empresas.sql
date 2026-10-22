CREATE TABLE empresas (
    id                UUID         NOT NULL,
    nome              VARCHAR(150) NOT NULL,
    cnpj              VARCHAR(14)  NOT NULL,
    email_corporativo VARCHAR(150) NOT NULL,
    telefone          VARCHAR(20),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em         TIMESTAMP(6) NOT NULL,
    versao            BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT uk_empresas_cnpj UNIQUE (cnpj)
);