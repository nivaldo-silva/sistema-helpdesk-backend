# Help Desk API

O Help Desk API é uma API REST desenvolvida para centralizar e organizar o atendimento de suporte técnico entre empresas fornecedoras de serviços e seus clientes corporativos (B2B).

O projeto surgiu da necessidade de estruturar o gerenciamento de chamados em um único sistema, permitindo que empresas clientes registrem e acompanhem solicitações, enquanto a equipe de suporte gerencia atendimentos, define prioridades, atualiza status e mantém o histórico das interações.

![Fluxo da API](images/fluxo-api.png)

## Perfis

| Perfil | Descrição |
|---|---|
| `CLIENTE` | Abre e acompanha os próprios chamados. |
| `SUPORTE` | Vê todos os chamados, aceita, altera status e prioridade e cria notas internas. |
| `ADMIN` | Tudo do suporte, mais gestão de empresas e usuários e exclusão de chamados. |

## Autenticação

A API usa JWT(JSON Web Token) para autenticação. Faça login em `POST /auth/login` com e-mail e senha e envie o token nas demais requisições:

```
Authorization: Bearer <token>
```

Rotas públicas: `POST /auth/login` e `POST /auth/registro`.

## Endpoints

| Recurso | Rotas |
|---|---|
| Autenticação | `POST /auth/login`, `POST /auth/registro` |
| Usuários | `POST /usuarios/clientes`, `POST /usuarios/admin/colaboradores`, `GET /usuarios`, `GET /usuarios/{id}`, `PUT /usuarios/{id}`, `PATCH /usuarios/{id}/desativar`, `PATCH /usuarios/{id}/reativar` |
| Empresas | `POST /empresas`, `GET /empresas`, `GET /empresas/{id}`, `PUT /empresas/{id}`, `PATCH /empresas/{id}/desativar`, `PATCH /empresas/{id}/reativar` |
| Chamados | `POST /chamados`, `GET /chamados`, `GET /chamados/abertos`, `GET /chamados/{id}`, `PUT /chamados/{id}`, `PATCH /chamados/{id}/aceitar`, `PATCH /chamados/{id}/status`, `PATCH /chamados/{id}/prioridade`, `DELETE /chamados/{id}` |
| Comentários | `POST /chamados/{id}/comentarios`, `GET /chamados/{id}/comentarios` |

## Fluxo básico

1. O ADMIN faz login e cadastra a empresa cliente e a equipe de suporte.
2. Os usuários da empresa entram por auto-cadastro ou são criados por alguém logado.
3. O CLIENTE abre um chamado.
4. O SUPORTE consulta a fila de abertos e aceita o chamado.
5. Cliente e suporte trocam comentários. Notas internas ficam visíveis só para a equipe.
6. O SUPORTE atualiza prioridade e status até o encerramento.

Status: `ABERTO` → `EM_ANDAMENTO` → `RESOLVIDO` → `FECHADO`, com `CANCELADO` como saída alternativa.

## Padrões utilizados na API

- IDs em UUID e datas no formato `dd/MM/yyyy HH:mm:ss`.
- Campos JSON em `snake_case`. Campos nulos não aparecem na resposta.
- Listas paginadas com `page` (a partir de 0), `size` e `sort`.
- Cliente só enxerga os próprios chamados. Um recurso fora da visibilidade do usuário retorna "não encontrado".

## Documentação

Com a aplicação rodando:

- Swagger UI: `/swagger-ui.html`
- OpenAPI: `/v3/api-docs`
