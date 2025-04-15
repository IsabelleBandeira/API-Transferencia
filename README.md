# Projeto API - Transferência

-------------------

Essa é uma API com os seguintes objetivos:
- **Cadastrar um Cliente**
- **Listar todos os clientes cadastrados**
- **Busca um cliente pelo número da conta**
- **Realizar Transferências entre contas**
- **Buscar transferências pelo número da conta**

---------------------------

[🔗 - Documentação da API (Swagger)](http://localhost:8080/swagger-ui.html)

### Essa solução utiliza:
- Java 17 LTS
- Maven
- SpringBoot 3.4.4
- Spring Data JPA
- H2 → Banco relacional in memory
- JUnit 5 
- Mockito
- SpringBoot Test + MockMvc
- Lombok

Projeto desenvolvido utilizando IntelliJ IDEA

--------------------

## Como rodar localmente


**Pré-requisitos:**
- Java 17+
- Maven 3.8+

**Passo a Passo:**
1. Clone o repo
2. Acesse o diretório usando a IDE de sua preferência
3. Instale as dependências e rode a aplicação
```
mvn clean install
mvn spring-boot:run

# Certifique-se de ter o Java e o Maven já instalados
```
4. Acesse a API usando Insomnia ou Postman ou como preferir
5. Acesse a [documentação da API](http://localhost:8080/swagger-ui.html) para mais informações de como cada endpoint
espera a chamada e o que ele retorna

### Como rodar os testes:
Após seguir os passos acima, execute os testes com o comando abaixo no terminal:
```
./mvnw test
```

## Espero que curta! ^^