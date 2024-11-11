
# API Finance - Sistema de Pagamentos e Análise de Fraudes

API para gerenciar pagamentos, verificar fraudes e armazenar transações financeiras. Esta aplicação é projetada para integrar com sistemas de pagamento e sistemas de verificação de fraude usando RabbitMQ para comunicação assíncrona.

## Tabela de Conteúdos
- [Requisitos](#requisitos)
- [Instalação](#instalacao)
- [Uso](#uso)
- [Endpoints da API](#endpoints-da-api)
- [Tecnologias](#tecnologias)
- [Contribuindo](#contribuindo)
- [Licença](#licenca)

## Requisitos
- Java 21
- Maven 3.8 ou superior
- PostgreSQL 13 ou superior
- RabbitMQ
- Docker

## Como iniciar o projeto

Este projeto foi criado usando o [Spring Initializr](https://start.spring.io/). Siga os passos abaixo para gerar e configurar o projeto:

1. **Gerar o Projeto:**
   - Acesse [https://start.spring.io/](https://start.spring.io/).
   - Selecione a versão do Spring Boot desejada (no nosso caso, usamos a versão mais recente).
   - Escolha o tipo de projeto: **Maven**.
   - Selecione o Java como a linguagem.
   - No campo "Group", coloque: `com.apifinance`.
   - No campo "Artifact", coloque: `jpa`.
   - Em "Dependencies", adicione:
     - **Spring Web**
     - **Spring Data JPA**
     - **Spring Boot DevTools** (opcional, para desenvolvimento)
     - **PostgreSQL Driver** (para o banco de dados)
     - **Spring Boot Starter AMQP** (para RabbitMQ)
   - Clique em **Generate** para baixar o arquivo ZIP do projeto.

2. **Descompactar e Importar:**
   - Descompacte o arquivo ZIP gerado.
   - Abra o projeto em sua IDE preferida (como IntelliJ IDEA ou VS Code).


## Instalação

1. Clone o repositório:

   ```bash
   git clone https://github.com/MichaelHCS/finance-api.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd api-finance
   ```

3. Configure o banco de dados PostgreSQL:

   - Crie o banco de dados `finance-api` no PostgreSQL.
   - Configure as credenciais de banco de dados no arquivo `application.properties` ou `application.yml`.

4. Execute o projeto:

   Com Maven:
   ```bash
   mvn spring-boot:run
   ```

   Com Docker:

   ```bash
   docker-compose up
   ```

5. O projeto estará disponível em `http://localhost:8080`.

## Uso
Após a instalação, a API estará rodando em `http://localhost:8080`. Você pode interagir com os seguintes endpoints da API.

### 1. Cadastro de Cliente
**Método:** POST  
**URL:** `http://localhost:8080/customers`  
**Corpo da Requisição:**
```json
{
  "name": "Ana Silva",
  "email": "ana.silva@gmail.com",
  "phoneNumber": "5541987654321"
}
```

### 2. Cadastro de Pagamento
**Método:** POST  
**URL:** `http://localhost:8080/payments`  
**Corpo da Requisição:**
```json
{
  "customer": {
    "id": "edd2b7a8-3065-4255-9e7d-71bf32790238"
  },
  "amount": 1400,
  "currency": "USD",
  "paymentType": "CREDIT_CARD"
}
```

### 3. Análise de Fraude
**Método:** POST  
**URL:** `http://localhost:8080/fraud-checks/analyze`  
**Corpo da Requisição:**
```json
{
  "paymentId": "aba93397-dcaf-4770-a0bf-0fb65b251d5f",
  "rabbitMqMessageId": "e237ff38-c06b-4ce7-9dd6-2effcab0f4d7",
  "fraudStatus": "APPROVED",
  "fraudReason": "NONE"
}
```
## Testando o RabbitMQ
Para verificar o status do RabbitMQ e gerenciar as filas, você pode acessar o painel de gerenciamento do RabbitMQ em:

- URL do painel: http://localhost:15672
- Credenciais padrão:
- Usuário: guest
- Senha: guest
O painel permite monitorar as filas, verificar mensagens e acompanhar o status dos consumidores.

## Tecnologias
- Spring Boot 
- PostgreSQL
- RabbitMQ
- JPA/Hibernate
- Maven

## Licença
Este projeto está licenciado sob a [MIT License](LICENSE).
