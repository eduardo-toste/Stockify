# 📦 Stockify API

Stockify é uma API para controle de estoque desenvolvida com **Java 23** e **Spring Boot**. A aplicação permite o gerenciamento de produtos, registro de movimentações de entrada e saída, e consulta de estatísticas. Conta com autenticação via **JWT**, documentação automatizada com **OpenAPI**, banco de dados **MySQL** gerenciado por **Docker e Flyway**, e uma cobertura sólida de **testes unitários com JUnit 5 e Mockito** para garantir a confiabilidade e qualidade das funcionalidades.

As listagens mais complexas são paginadas para melhorar a usabilidade, e os dados podem ser exportados em formato **Excel**. A estrutura segue boas práticas com separação em camadas, tratamento global de exceções, uso de JPA com Hibernate para persistência e uma arquitetura preparada para evolução contínua.

![image](https://github.com/user-attachments/assets/31d36a60-e2c1-4ff1-8e6a-8ebe1a1c5f11)

---

## 🚀 Tecnologias Utilizadas

### Backend
- Java 23
- Spring Boot
- Spring Web
- Spring Security (Autenticação JWT)
- Spring Data JPA
- Hibernate

### Banco de Dados
- MySQL (via Docker)
- Flyway (Gerenciamento de Migração)

### Testes
- JUnit 5 (Testes unitários)
- Mockito (Mock de dependências)
- Spring Boot Test

### Documentação
- OpenAPI (SpringDoc)

### Ferramentas e Build
- Maven
- Docker

---

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
│── models (Modelos e Entidades do banco de dados)
│   ├── enums (Enums)
│── repositories (Interfaces de repositórios JPA)
│── services (Regras de negócio e validações)
│   ├── validations (Classes e Interfaces de validação)
│── utils (Funções auxiliares)
│── resources (Scripts SQL para migração do banco)
```

## 📌 Endpoints Disponíveis

### Autenticação
- `POST /auth/login` → Autenticar usuário e gerar token
- `POST /auth/register` → Cadastrar um novo usuário

### Produtos
- `POST /produtos` → Cadastrar um novo produto
- `GET /produtos` → Listar todos os produtos
- `GET /produtos/{id}` → Buscar um produto específico
- `PUT /produtos/{id}` → Atualizar informações do produto
- `DELETE /produtos/{id}` → Remover um produto
- `GET /produtos/exportar` → Exportar lista de produtos (xlsx)

### Movimentações de Estoque
- `POST /movimentacao` → Registrar entrada/saída de produtos
- `GET /movimentacao` → Listar movimentações de estoque
- `GET /movimentacao/{id}` → Buscar movimentação específica
- `GET /movimentacao/exportar` → Exportar lista de movimentações (xlsx)

### Estatísticas
- `GET /estatisticas` → Consultar estatísticas do estoque

## ⚙️ Configuração e Execução

### Pré-requisitos
Certifique-se de ter as seguintes dependências instaladas:
- Java 23
- Docker
- Maven

### Configuração do Banco de Dados
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

### Executando a Aplicação
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
