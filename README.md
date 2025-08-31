# Stockify

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
![AWS](https://img.shields.io/badge/AWS-S3-FF9900?style=for-the-badge&logo=amazon-aws)

Stockify é uma API robusta para controle de estoque desenvolvida com **Java 21** e **Spring Boot 3.4.4**. A aplicação oferece gerenciamento completo de produtos, movimentações de entrada e saída, autenticação segura via **JWT**, exportação de dados para **Excel**, integração com **AWS S3** para armazenamento de arquivos, e uma cobertura abrangente de **testes unitários** com JUnit 5 e Mockito.

A solução implementa boas práticas de desenvolvimento com arquitetura em camadas, tratamento global de exceções, paginação para listagens, migração de banco de dados com **Flyway**, documentação automática via **OpenAPI/Swagger**, e monitoramento com **Spring Boot Actuator**.

![Stockify Logo](https://github.com/user-attachments/assets/31d36a60-e2c1-4ff1-8e6a-8ebe1a1c5f11)

---

## 📑 Sumário

- [Visão Geral](#-visão-geral)
- [Badges](#-badges)
- [Tecnologias e Ferramentas](#-tecnologias-e-ferramentas)
- [Arquitetura e Funcionalidades](#-arquitetura-e-funcionalidades)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Build, Execução e Parada](#-build-execução-e-parada)
- [Banco de Dados e Migrações](#-banco-de-dados-e-migrações)
- [Segurança](#-segurança)
- [Testes](#-testes)
- [Documentação da API](#-documentação-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Endpoints Principais e Exemplos](#-endpoints-principais-e-exemplos)
- [Práticas de Produção](#-práticas-de-produção)
- [Contribuição](#-contribuição)
- [Licença](#-licença)
- [Contato](#-contato)

---

## 🎯 Visão Geral

Stockify foi projetado para ser uma solução completa e escalável para gestão de estoque, com foco em:

- **Gerenciamento de Produtos**: Cadastro, listagem, atualização e exclusão de produtos com categorização
- **Controle de Movimentações**: Registro de entradas e saídas com validação de estoque
- **Autenticação Segura**: Sistema JWT com refresh tokens para segurança robusta
- **Exportação de Dados**: Geração de relatórios em Excel com upload automático para AWS S3
- **Estatísticas**: Dashboard com métricas de estoque e movimentações
- **Monitoramento**: Health checks e métricas via Spring Boot Actuator

---

## 🏆 Badges

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen?style=for-the-badge&logo=spring)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-red?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
![AWS S3](https://img.shields.io/badge/AWS-S3-FF9900?style=for-the-badge&logo=amazon-aws)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-green?style=for-the-badge&logo=openapi)
![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?style=for-the-badge&logo=junit5)
![Flyway](https://img.shields.io/badge/Flyway-Migration-CC0200?style=for-the-badge&logo=flyway)

---

## 🛠 Tecnologias e Ferramentas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - API REST
- **Spring Boot Actuator** - Monitoramento e métricas

### Banco de Dados
- **MySQL 8.0** - Banco de dados principal
- **Flyway** - Migração e versionamento do banco
- **H2** - Banco em memória para testes

### Segurança
- **JWT (Auth0)** - Tokens de autenticação
- **BCrypt** - Criptografia de senhas
- **Spring Security** - Configurações de segurança

### Cloud e Armazenamento
- **AWS S3** - Armazenamento de arquivos
- **AWS Secrets Manager** - Gerenciamento de secrets

### Documentação e Testes
- **OpenAPI/Swagger** - Documentação automática da API
- **JUnit 5** - Framework de testes
- **Mockito** - Mock de dependências
- **Spring Boot Test** - Testes de integração

### Ferramentas de Build
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização
- **Lombok** - Redução de boilerplate

---

## 🏗 Arquitetura e Funcionalidades

### 1. Sistema de Autenticação
- **JWT com Refresh Tokens**: Autenticação stateless com renovação automática
- **BCrypt**: Senhas criptografadas com salt automático
- **Filtros de Segurança**: Validação automática de tokens em todas as requisições
- **Tratamento de Exceções**: Respostas padronizadas para erros de autenticação

### 2. Gerenciamento de Produtos
- **CRUD Completo**: Criação, leitura, atualização e exclusão de produtos
- **Validação de Duplicatas**: Prevenção de produtos com nomes duplicados
- **Categorização**: Sistema de categorias para organização
- **Paginação**: Listagens otimizadas com paginação automática

### 3. Controle de Movimentações
- **Entrada e Saída**: Registro de movimentações com validação de estoque
- **Controle de Estoque**: Atualização automática de quantidades
- **Validação de Disponibilidade**: Prevenção de saídas com estoque insuficiente
- **Auditoria**: Histórico completo de todas as movimentações

### 4. Exportação e Relatórios
- **Excel Export**: Geração de relatórios em formato Excel
- **AWS S3 Integration**: Upload automático de arquivos para nuvem
- **URLs Pré-assinadas**: Acesso seguro aos arquivos exportados
- **Estatísticas**: Dashboard com métricas de estoque

### 5. Monitoramento e Observabilidade
- **Health Checks**: Endpoint de saúde da aplicação
- **Métricas**: Coleta de métricas via Actuator
- **Logs Estruturados**: Logging configurado com Logback

---

## 🔧 Variáveis de Ambiente

### Configurações do Banco de Dados
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stockify
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Configurações AWS
```properties
cloud.aws.region.static=us-east-1
aws.s3.bucket-name=stockify-files-bucket
```

### Configurações de Segurança
```properties
# JWT Secret (deve ser configurado via AWS Secrets Manager em produção)
api.security.token.secret=seu-jwt-secret-aqui
```

### Configurações de Monitoramento
```properties
management.endpoints.web.exposure.include=health,info,metrics
```

---

## 🚀 Build, Execução e Parada

### Pré-requisitos
- **Java 21** ou superior
- **Docker** e **Docker Compose**
- **Maven 3.6+**

### Executando com Docker Compose
```bash
# Subir toda a stack (aplicação + banco)
docker-compose up -d

# Verificar logs
docker-compose logs -f

# Parar a stack
docker-compose down
```

### Executando Localmente
```bash
# 1. Subir apenas o banco de dados
docker-compose up mysql -d

# 2. Executar a aplicação
./mvnw spring-boot:run

# 3. Acessar a aplicação
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Executando Testes
```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=ProdutoServiceTest

# Testes com cobertura
./mvnw test jacoco:report
```

### Build para Produção
```bash
# Gerar JAR
./mvnw clean package -DskipTests

# Build com Docker
docker build -t stockify:latest .
```

---

## 🗄 Banco de Dados e Migrações

### Estrutura do Banco
- **MySQL 8.0** como banco principal
- **Flyway** para versionamento e migração automática
- **H2** para testes unitários

### Tabelas Principais
- `produtos` - Cadastro de produtos
- `usuarios` - Usuários do sistema
- `movimentacoes` - Histórico de movimentações

### Migrações
As migrações estão localizadas em `src/main/resources/db/migration/`:
- `V1__create-table-produtos.sql`
- `V2__create-table-users.sql`
- `V3__create-table-movimentacoes.sql`

### Executando Migrações
```bash
# Migrações automáticas (executam na inicialização)
./mvnw spring-boot:run

# Verificar status das migrações
./mvnw flyway:info
```

---

## 🔐 Segurança

### Autenticação JWT
- **Access Tokens**: Válidos por 1 hora
- **Refresh Tokens**: Válidos por 7 dias
- **Renovação Automática**: Endpoint `/auth/refresh`

### Configurações de Segurança
- **CORS**: Configurado para origens específicas
- **CSRF**: Desabilitado para APIs stateless
- **Session Management**: Stateless (sem sessões)
- **Password Encoding**: BCrypt com salt automático

### Endpoints Protegidos
- Todos os endpoints exceto `/auth/*` e `/swagger-ui/**` requerem autenticação
- Validação automática de tokens via `SecurityFilter`

### Exemplo de Uso
```bash
# 1. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Usar token nas requisições
curl -X GET http://localhost:8080/produtos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🧪 Testes

### Cobertura de Testes
- **Testes Unitários**: Services, Controllers, Repositories
- **Testes de Integração**: Endpoints completos
- **Testes de Segurança**: Autenticação e autorização
- **Mocks**: Dependências externas (AWS S3, etc.)

### Estrutura de Testes
```
src/test/java/
├── controllers/          # Testes de controllers
├── services/            # Testes de serviços
├── repositories/        # Testes de repositórios
├── config/             # Configurações de teste
└── utils/              # Utilitários para testes
```

### Executando Testes
```bash
# Todos os testes
./mvnw test

# Testes com relatório de cobertura
./mvnw test jacoco:report

# Testes específicos
./mvnw test -Dtest=ProdutoServiceTest
```

### Configurações de Teste
- **H2 Database**: Banco em memória para testes
- **TestContainers**: Isolamento de testes (quando necessário)
- **MockMvc**: Testes de endpoints REST

---

## 📚 Documentação da API

### Swagger/OpenAPI
A documentação interativa está disponível em:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Principais Endpoints

#### Autenticação
- `POST /auth/login` - Autenticação de usuário
- `POST /auth/register` - Cadastro de usuário
- `POST /auth/refresh` - Renovação de token

#### Produtos
- `GET /produtos` - Listar produtos (paginado)
- `POST /produtos` - Criar produto
- `GET /produtos/{id}` - Buscar produto por ID
- `PUT /produtos/{id}` - Atualizar produto
- `DELETE /produtos/{id}` - Excluir produto
- `GET /produtos/exportar` - Exportar produtos para Excel

#### Movimentações
- `GET /movimentacao` - Listar movimentações (paginado)
- `POST /movimentacao` - Registrar movimentação
- `GET /movimentacao/{id}` - Buscar movimentação por ID
- `GET /movimentacao/exportar` - Exportar movimentações para Excel

#### Estatísticas
- `GET /estatisticas` - Obter estatísticas do estoque

---

## 📁 Estrutura do Projeto

```
src/main/java/com/eduardo/stockify/
├── aws/
│   ├── s3/                    # Integração AWS S3
│   └── secrets/               # Gerenciamento de secrets
├── config/
│   ├── documentation/         # Configuração OpenAPI
│   └── security/              # Configurações de segurança
├── controllers/               # Controladores REST
├── dtos/                      # Data Transfer Objects
├── exceptions/                # Exceções customizadas
├── mapper/                    # Mappers para conversão de objetos
├── models/
│   ├── enums/                 # Enumeradores
│   ├── Movimentacao.java      # Entidade Movimentação
│   ├── Produto.java           # Entidade Produto
│   └── Usuario.java           # Entidade Usuário
├── repositories/              # Repositórios JPA
├── services/                  # Lógica de negócio
├── utils/                     # Utilitários
└── StockifyApplication.java   # Classe principal

src/main/resources/
├── application.properties     # Configurações da aplicação
├── db/migration/              # Scripts de migração Flyway
└── logback-spring.xml         # Configuração de logs

src/test/java/                 # Testes unitários e de integração
```

---

## 🔗 Endpoints Principais e Exemplos

### Autenticação

#### Login
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "admin@stockify.com",
  "password": "admin123"
}

# Resposta
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Registro
```bash
POST /auth/register
Content-Type: application/json

{
  "username": "novo@usuario.com",
  "password": "senha123"
}
```

### Produtos

#### Criar Produto
```bash
POST /produtos
Authorization: Bearer SEU_TOKEN
Content-Type: application/json

{
  "nome": "Notebook Dell",
  "descricao": "Notebook Dell Inspiron 15 3000",
  "preco": 2500.00,
  "quantidade": 10,
  "categoria": "ELETRONICOS"
}
```

#### Listar Produtos (Paginado)
```bash
GET /produtos?page=0&size=10&sort=nome,asc
Authorization: Bearer SEU_TOKEN
```

#### Exportar Produtos
```bash
GET /produtos/exportar
Authorization: Bearer SEU_TOKEN

# Resposta
{
  "fileName": "produtos_2024-01-15.xlsx",
  "downloadUrl": "https://s3.amazonaws.com/bucket/produtos_2024-01-15.xlsx"
}
```

### Movimentações

#### Registrar Entrada
```bash
POST /movimentacao
Authorization: Bearer SEU_TOKEN
Content-Type: application/json

{
  "produtoId": 1,
  "quantidade": 5,
  "tipo": "ENTRADA"
}
```

#### Registrar Saída
```bash
POST /movimentacao
Authorization: Bearer SEU_TOKEN
Content-Type: application/json

{
  "produtoId": 1,
  "quantidade": 2,
  "tipo": "SAIDA"
}
```

### Estatísticas

#### Obter Estatísticas
```bash
GET /estatisticas
Authorization: Bearer SEU_TOKEN

# Resposta
{
  "totalProdutos": 150,
  "totalMovimentacoes": 1250,
  "valorTotalEstoque": 125000.00,
  "produtosBaixoEstoque": 5
}
```

---

## 🏭 Práticas de Produção

### Configurações de Produção
- **Secrets Management**: Use AWS Secrets Manager para JWT secrets
- **Database**: Configure connection pooling e SSL
- **Monitoring**: Configure métricas e alertas
- **Logging**: Configure logs estruturados (JSON)

### Variáveis de Ambiente Recomendadas
```bash
# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/stockify
SPRING_DATASOURCE_USERNAME=stockify_user
SPRING_DATASOURCE_PASSWORD=senha_segura

# AWS
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=stockify-prod-files

# JWT
JWT_SECRET=seu-secret-super-seguro-aqui

# Logging
LOGGING_LEVEL_COM_EDUARDO_STOCKIFY=INFO
```

### Docker para Produção
```dockerfile
FROM openjdk:21-jre-slim
COPY target/stockify-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Health Checks
```bash
# Verificar saúde da aplicação
curl http://localhost:8080/actuator/health

# Métricas
curl http://localhost:8080/actuator/metrics
```

---

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. **Fork** o repositório
2. Crie uma **branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. **Commit** suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um **Pull Request**

### Padrões de Código
- Siga as convenções do Java
- Use Lombok para reduzir boilerplate
- Escreva testes para novas funcionalidades
- Documente APIs com OpenAPI annotations

### Issues
- Use o template de issue apropriado
- Forneça informações detalhadas sobre bugs
- Para features, descreva o caso de uso

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">

**⭐ Se este projeto foi útil para você, considere dar uma estrela! ⭐**

</div>