# DSCommerce

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## 📑 Índice

- [📋 Sobre o Projeto](#-sobre-o-projeto)
- [🚀 Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [🏗️ Arquitetura](#-arquitetura)
- [📦 Modelo de Domínio](#-modelo-de-domínio)
- [⚙️ Funcionalidades Implementadas](#-funcionalidades-implementadas)
- [🛠️ Instalação e Configuração](#-instalação-e-configuração)
- [🔧 Perfis de Ambiente](#-perfis-de-ambiente)
- [🔐 Autenticação](#-autenticação)
- [📚 API Endpoints](#-api-endpoints)
- [🎯 Features Planejadas](#-features-planejadas)
  - [🔄 Em Desenvolvimento](#-em-desenvolvimento)
  - [🚀 Melhorias Técnicas](#-melhorias-técnicas)
- [🧪 Testes](#-testes)
- [👨‍💻 Autor](#-autor)

## 📋 Sobre o Projeto

DSCommerce é uma aplicação completa de e-commerce desenvolvida com Spring Boot 3, implementando as melhores práticas de desenvolvimento backend com Java. O projeto segue uma arquitetura em camadas bem definida, com foco em segurança, performance e escalabilidade.

Este é um projeto desenvolvido durante o curso **Java Spring Professional** da **DevSuperior**, ministrado pelo professor **Nélio Alves**, com implementações e melhorias adicionais.

---
## 🚀 Tecnologias Utilizadas

### Core
- **Java 21** - Última versão LTS do Java
- **Spring Boot 3.5.7** - Framework principal
- **Maven** - Gerenciamento de dependências

### Persistência
- **Spring Data JPA** - Abstração de acesso a dados
- **Hibernate** - ORM para mapeamento objeto-relacional
- **PostgreSQL** - Banco de dados de produção
- **H2 Database** - Banco de dados em memória para testes

### Segurança
- **Spring Security** - Framework de autenticação e autorização
- **OAuth 2.0** - Protocolo de autorização
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Authorization Server** - Servidor de autenticação customizado

### Validação
- **Spring Validation** - Validação de dados
- **Hibernate Validator 8.0** - Implementação da Bean Validation

### DevOps
- **Docker & Docker Compose** - Containerização da aplicação
- **PostgreSQL 17 Alpine** - Banco de dados containerizado

---
## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/example/dscommerce/
├── config/              # Configurações de segurança e servidor
│   ├── AuthorizationServerConfig
│   ├── ResourceServerConfig
│   └── customgrant/
├── controllers/         # Camada de apresentação (API REST)
│   ├── CategoryController
│   ├── OrderController
│   ├── ProductController
│   ├── UserController
│   └── handlers/        # Tratamento global de exceções
├── dto/                 # Data Transfer Objects
│   ├── CategoryDTO
│   ├── OrderDTO
│   ├── ProductDTO
│   ├── UserDTO
│   └── ValidationError
├── entities/            # Entidades JPA
│   ├── Category
│   ├── Order
│   ├── OrderItem
│   ├── Payment
│   ├── Product
│   ├── Role
│   └── User
├── repositories/        # Camada de acesso a dados
├── services/            # Lógica de negócio
│   ├── AuthService
│   ├── CategoryService
│   ├── OrderService
│   ├── ProductService
│   └── UserService
└── projections/         # Projeções para consultas otimizadas
```

---
## 📦 Modelo de Domínio

### Entidades Principais

- **User** - Usuários do sistema com roles (ADMIN, CLIENT)
- **Product** - Produtos do catálogo
- **Category** - Categorias de produtos (relacionamento Many-to-Many)
- **Order** - Pedidos realizados pelos clientes
- **OrderItem** - Itens do pedido (entidade associativa com chave composta)
- **Payment** - Pagamentos vinculados aos pedidos
- **Role** - Papéis de autorização dos usuários

### Relacionamentos

- User ←→ Order (One-to-Many)
- Order ←→ OrderItem (One-to-Many)
- Order ←→ Payment (One-to-One)
- Product ←→ OrderItem (One-to-Many)
- Product ←→ Category (Many-to-Many)
- User ←→ Role (Many-to-Many)

---
## ⚙️ Funcionalidades Implementadas

### Autenticação e Autorização
- ✅ Sistema completo de autenticação OAuth 2.0
- ✅ Geração e validação de JWT tokens
- ✅ Autorização baseada em roles (ROLE_ADMIN, ROLE_CLIENT)
- ✅ Proteção de endpoints por perfil de usuário
- ✅ Custom Grant Types para OAuth2

### Gestão de Produtos
- ✅ CRUD completo de produtos
- ✅ Listagem paginada de produtos
- ✅ Busca de produtos por nome
- ✅ Associação de produtos com múltiplas categorias
- ✅ Validação de dados de entrada

### Gestão de Categorias
- ✅ Listagem de todas as categorias
- ✅ Associação de categorias com produtos

### Gestão de Pedidos
- ✅ Criação de pedidos
- ✅ Consulta de pedidos por ID
- ✅ Listagem de pedidos do usuário autenticado
- ✅ Cálculo automático do total do pedido
- ✅ Controle de status do pedido (WAITING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELED)

### Gestão de Usuários
- ✅ Obter dados do usuário autenticado
- ✅ Validação de permissões por usuário

### Tratamento de Erros
- ✅ Tratamento global de exceções
- ✅ Mensagens de erro padronizadas
- ✅ Validação de campos com feedback detalhado

---
## 🛠️ Instalação e Configuração

### Pré-requisitos

- Java 21 ou superior
- Maven 3.8+
- Docker e Docker Compose (opcional, para banco de dados)
- PostgreSQL 17 (se não usar Docker)

### 1. Clone o repositório

```bash
git clone <repository-url>
cd DSEcommerce
```

### 2. Configure o banco de dados

#### Opção A: Usando Docker (Recomendado)

```bash
docker-compose up -d
```

O banco de dados estará disponível em `localhost:5433`

#### Opção B: PostgreSQL Local

Configure as propriedades em `application-prod.properties` ou `application-staging.properties`

### 3. Configure as variáveis de ambiente (opcional)

```bash
export APP_PROFILE=staging
export CLIENT_ID=myclientid
export CLIENT_SECRET=myclientsecret
export JWT_DURATION=86400
export CORS_ORIGINS=http://localhost:3000,http://localhost:5173
```

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

Ou construa o JAR e execute:

```bash
./mvnw clean package
java -jar target/dscommerce-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em `http://localhost:8081`

---
## 🔧 Perfis de Ambiente

O projeto possui três perfis configurados:

- **test** - Usa H2 em memória, ideal para testes
- **staging** - Ambiente de desenvolvimento com PostgreSQL
- **prod** - Ambiente de produção

Para alterar o perfil, modifique a variável `APP_PROFILE` ou a propriedade `spring.profiles.active`

---
## 🔐 Autenticação

### Obter Token de Acesso

```bash
POST http://localhost:8081/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
username=maria@gmail.com
password=123456
client_id=myclientid
client_secret=myclientsecret
```

### Usar Token nas Requisições

```bash
GET http://localhost:8081/products
Authorization: Bearer {access_token}
```

---
## 📚 API Endpoints

### Produtos
- `GET /products` - Lista produtos (paginado)
- `GET /products/{id}` - Busca produto por ID
- `POST /products` - Cria novo produto (ADMIN)
- `PUT /products/{id}` - Atualiza produto (ADMIN)
- `DELETE /products/{id}` - Remove produto (ADMIN)

### Categorias
- `GET /categories` - Lista todas as categorias

### Pedidos
- `GET /orders/{id}` - Busca pedido por ID
- `POST /orders` - Cria novo pedido (CLIENT)

### Usuários
- `GET /users/me` - Retorna dados do usuário autenticado

---
## 🎯 Features Planejadas

### 🔄 Em Desenvolvimento

#### 1. Carrinho de Compras Persistente
- [ ] Salvar carrinho de compras no banco de dados
- [ ] Recuperar carrinho entre sessões
- [ ] API REST para gerenciar itens do carrinho
- [ ] Sincronização em tempo real com estoque disponível

#### 2. Integração com Gateway de Pagamento (Simulado)
- [ ] Implementar integração com Stripe Sandbox ou Pagar.me
- [ ] Criar Webhook Controller para notificações de pagamento (Status: Aprovado, Recusado, Pendente)
- [ ] Configurar processamento assíncrono para conciliação de pagamentos com AWS SQS
- [ ] Simular cenários de erro (Cartão sem saldo, Timeout e Fraude)
- [ ] Implementar suporte a múltiplos métodos (Pix, Cartão e Boleto)

#### 3. Sistema de Notificações Assíncronas
- [ ] Implementar message broker com Apache Kafka ou RabbitMQ
- [ ] Criar producers para eventos de domínio (OrderCreated, OrderPaid, OrderShipped, etc.)
- [ ] Desenvolver consumers para processamento de notificações
- [ ] Implementar Dead Letter Queue (DLQ) para tratamento de falhas
- [ ] Configurar retry policies e circuit breaker patterns
- [ ] Criar dashboard de monitoramento de mensagens (Kafka UI ou RabbitMQ Management)

### 🚀 Melhorias Técnicas

#### 1. Testes Unitários, de Integração e QA
- [ ] Criar testes unitários para Services com JUnit 5 e Mockito
- [ ] Testar regras de negócio (cálculos, validações)
- [ ] Testar Controllers
- [ ] Testar Repositories
- [ ] Testar autenticação e autorização OAuth2
- [ ] Testar fluxos completos
- [ ] Configurar relatórios de cobertura com JaCoCo (70%+)

#### 2. Documentação de API
- [ ] Implementar Swagger UI com SpringDoc para documentação interativa
- [ ] Documentar endpoints com exemplos de requisição e resposta
- [x] Criar collections do Postman/Insomnia para testes de API
- [ ] Gerar documentação OpenAPI 3.0
- [ ] Adicionar exemplos de autenticação OAuth2 na documentação
- [ ] Implementar versionamento de API (v1, v2)

#### 3. Monitoramento e Logs
- [ ] Configurar Spring Boot Actuator para health checks e métricas
- [ ] Implementar observabilidade com OpenTelemetry para tracing distribuído
- [ ] Integrar Prometheus para coleta de métricas
- [ ] Criar dashboards no Grafana para visualização de métricas
- [ ] Implementar logging estruturado com correlação de requisições

---
## 🧪 Testes

> ⚠️ **Status:** Em desenvolvimento

---
## 👨‍💻 Autor

Desenvolvido por [Lucas Lotar](https://lotar.dev.br/) durante o curso Java Spring Professional da DevSuperior.
