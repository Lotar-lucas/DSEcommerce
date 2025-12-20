# Fluxo de Spring Security e OAuth2 no Projeto

Este projeto implementa **Authorization Server** e **Resource Server** no mesmo aplicativo usando Spring Security OAuth2. O fluxo acontece em momentos diferentes dependendo se o cliente está **autenticando** (gerando token) ou **acessando recursos** (validando token).

## Steps

### 1. Requisição de Token (Authorization Server entra em ação)
Quando o cliente faz POST para `/oauth2/token` com credenciais (`grant_type=password`, `username`, `password`), o **AuthorizationServerConfig** (Order 2) processa via:
- `CustomPasswordAuthenticationConverter`
- `CustomPasswordAuthenticationProvider`

### 2. Autenticação de Usuário
O `CustomPasswordAuthenticationProvider` chama `UserService.loadUserByUsername` que:
- Consulta o banco via `UserRepository.searchUserAndRolesByEmail`
- Valida credenciais com `BCryptPasswordEncoder`

### 3. Geração do JWT
Após validação bem-sucedida, o `tokenGenerator`:
- Cria um JWT self-contained com claims customizadas (`authorities`, `username`)
- Usa RSA keys geradas dinamicamente
- Retorna o `access_token` para o cliente

### 4. Requisições aos Endpoints (Resource Server entra em ação)
Quando o cliente acessa endpoints REST (ex: `/products`), o **ResourceServerConfig** (Order 3):
- Intercepta a requisição
- Valida o JWT no header `Authorization`
- Extrai `authorities` usando `JwtAuthenticationConverter`
- Disponibiliza no `SecurityContextHolder`

### 5. Controle de Acesso
Atualmente todos endpoints permitem acesso de forma global (`permitAll()`), mas com `@EnableMethodSecurity` ativo, durante o desenvolvimento será colocado restrições em cada endpoint conforme necessário.

## Further Considerations

### Ordem de execução
1. **H2 Console** (Order 1, perfil test)
2. **Authorization Server** (Order 2)
3. **Resource Server** (Order 3)

O Spring Security avalia qual `SecurityFilterChain` usar baseado nos matchers.

### Melhorias de segurança
- Considerar trocar `permitAll()` por controle baseado em roles
- Adicionar `@PreAuthorize("hasRole('ADMIN')")` em métodos sensíveis do `ProductController`
- Implementar autorização granular por operação (GET, POST, PUT, DELETE)

### Centralização
- **Authorization Server** e **Resource Server** no mesmo projeto simplifica desenvolvimento
- Em **produção**, considerar separar para:
  - Melhor escalabilidade
  - Segurança aprimorada
  - Possibilidade de múltiplos Resource Servers

## Fluxo Visual

```
┌─────────────────────────────────────────────────────────────────┐
│                    AUTENTICAÇÃO (Login)                          │
└─────────────────────────────────────────────────────────────────┘
Cliente → POST /oauth2/token (username, password, grant_type)
    ↓
AuthorizationServerConfig (Order 2)
    ↓
CustomPasswordAuthenticationConverter
    ↓
CustomPasswordAuthenticationProvider
    ↓
UserService.loadUserByUsername()
    ↓
UserRepository.searchUserAndRolesByEmail()
    ↓
BCryptPasswordEncoder.matches()
    ↓
tokenGenerator (JWT com authorities e username)
    ↓
Cliente ← access_token (JWT)

┌─────────────────────────────────────────────────────────────────┐
│                  AUTORIZAÇÃO (Acesso aos recursos)               │
└─────────────────────────────────────────────────────────────────┘
Cliente → GET /products (Authorization: Bearer <JWT>)
    ↓
ResourceServerConfig (Order 3)
    ↓
Validação do JWT (assinatura + expiração)
    ↓
JwtAuthenticationConverter (extrai authorities do token)
    ↓
SecurityContextHolder (armazena autenticação)
    ↓
ProductController (acesso permitido ou negado baseado em @PreAuthorize)
    ↓
Cliente ← Resposta (200 OK ou 403 Forbidden)
```

## Configurações Importantes

### application.properties
```properties
security.client-id=${CLIENT_ID:seu-client-id}
security.client-secret=${CLIENT_SECRET:seu-client-secret}
security.jwt.duration=${JWT_DURATION:86400}
cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}
```

### Endpoints OAuth2
- **Token endpoint**: `POST /oauth2/token`
- **JWK Set**: `GET /.well-known/jwks.json`
- **Authorization Server Metadata**: `GET /.well-known/oauth-authorization-server`

## Exemplo de Requisição de Token

```bash
curl -X POST http://localhost:{PORT}/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&username={seu-email}&password={sua-senha}&client_id={seu-client-id}&client_secret={seu-client-secret}"
```

## Exemplo de Requisição Autenticada

```bash
curl -X GET http://localhost:{PORT}/products \
  -H "Authorization: Bearer <access_token>"
```

