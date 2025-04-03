# 📦 Stockify API

Stockify é uma API de gerenciamento de estoque desenvolvida com Java e Spring Boot. Esta API permite gerenciar produtos e usuários, garantindo autenticação segura com JWT.

## 🚀 Tecnologias Utilizadas

### 📌 Backend
- Java 23
- Spring Boot
- Spring Security (Autenticação JWT)
- Spring Data JPA
- Hibernate

### 📌 Banco de Dados
- MySQL (via Docker)
- Flyway (Gerenciamento de Migração)

### 📌 Documentação
- OpenAPI (SpringDoc)

### 📌 Ferramentas e Build
- Maven
- Docker

## 📂 Estrutura do Projeto

```
com.eduardo.stockify
│── config
│   ├── documentation (Configuração do SpringDoc)
│   ├── infrastructure
│   ├── security (Configuração de segurança e filtros)
│── controllers (Controladores da API)
│── dtos (Objetos de Transferência de Dados)
│── exceptions (Exceções personalizadas)
│── models (Modelos do banco de dados)
│── repositories (Interfaces de repositórios JPA)
│── services (Regras de negócio e validações)
│── utils (Funções auxiliares)
│── resources (Scripts SQL para migração do banco)
```

## 📌 Endpoints Disponíveis

### 🔐 Autenticação
- `POST /auth/login` → Autenticar usuário e gerar token
- `POST /auth/register` → Cadastrar um novo usuário

### 📦 Produtos
- `POST /produtos` → Cadastrar um novo produto
- `GET /produtos` → Listar todos os produtos
- `GET /produtos/{id}` → Buscar um produto específico
- `PUT /produtos/{id}` → Atualizar informações do produto
- `DELETE /produtos/{id}` → Remover um produto

## ⚙️ Configuração e Execução

### 📌 Pré-requisitos
Certifique-se de ter as seguintes dependências instaladas:
- Java 23
- Docker
- Maven

### 🔧 Configuração do Banco de Dados
A aplicação utiliza um banco de dados MySQL via Docker. Para configurá-lo, execute o seguinte comando:
```sh
docker run --name stockify-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=stockify -p 3306:3306 -d mysql:latest
```
Edite o arquivo `application.properties` com as credenciais do banco:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stockify
spring.datasource.username=root
spring.datasource.password=root
```

### ▶️ Executando a Aplicação
1. Clone o repositório:
   ```sh
   git clone https://github.com/eduardo-toste/stockify.git
   ```
2. Navegue até a pasta do projeto:
   ```sh
   cd stockify
   ```
3. Execute o projeto com Maven:
   ```sh
   mvn spring-boot:run
   ```
4. Acesse a documentação da API via OpenAPI:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 🤝 Contribuição
Se deseja contribuir com melhorias, faça um fork do repositório, crie uma branch e envie um pull request.