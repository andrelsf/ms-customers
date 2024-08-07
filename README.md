# Microservice Customers

API Customers 
Features
- Cadastro de clientes e contas.
- Listagem de todos os clientes.
- Filtro na listagem por numero da conta.
- Transferencia bancaria entre contras, com valor maximo da transferencia de R$10.000,00.
- Bloqueio de concorrencia nas transferencias.
- Registro de tentativas de transferencia com sucesso e falhas.
- Listagem das transferencias realizadas de cada usuario.

### Requirements
 - Java 21
 - Maven 3.8+
 - IDE VSCode or Intellij
 - NOTA: Base de dados H2, ou seja, base em memoria.
 - Design de desenvolvimento em Camadas: Controller, Service e repository

### Recursos

| Method |             Headers             | Resource                                 | Description                                             |
|:------:|:-------------------------------:|:-----------------------------------------|:--------------------------------------------------------|
|  POST  | Content-Type: application/json  | /api/v1/customers                        | Registra um novo cliente e sua conta.                    |
|  GET   | Content-Type: application/json  | /api/v1/customers                        | Obtem a listagem de todos os clientes cadastros.        |
|  GET   | Content-Type: application/json  | /api/v1/customers?accountNumber=1234     | Filtragem de todos os clientes pelo numero da conta.    |
|  GET   | Content-Type: application/json  | /api/v1/customers?page=0&size=10         | Controle de paginacao.                                  |
|  GET   | Content-Type: application/json  | /api/v1/customers/{customerId}           | Obtem o registro do cliente pelo ID.                    |
|  POST  | Content-Type: application/json  | /api/v1/customers/{customerId}/transfers | Realiza transferencia entre contas.                     |
|  GET   | Content-Type: application/json  | /api/v1/customers/{customerId}/transfers | Obtem a listagem de todas as transferencias realizadas. |

### Exemplos com cURL

#### Registra um novo cliente
```shell
curl --request POST \
--location 'http://localhost:8091/api/v1/customers' \
--header 'Content-Type: application/json; charset=utf-8' \
--data '{
    "name": "Jose Nome Facil",
    "cpf": "11122233344",
    "account": {
        "agency": 9876,
        "accountNumber": 1007659
    }
}'
```

---

#### Obtem a listagem de todos os clientes registrados paginado.

```shell
curl --request GET \
--location 'http://localhost:8091/api/v1/customers?page=0&size=10' \
--header 'Content-Type: application/json; charset=utf-8'
```

---

#### Obtem o registro de um unico cliente pelo ID

```shell
curl --request GET \
--location 'http://localhost:8091/api/v1/customers/3d05773e-513e-11ef-85b4-938a0beed59a' \
--header 'Content-Type: application/json; charset=utf-8'
```

---

#### Realiza filtro no recurso clientes pelo numero da conta.

```shell
curl --request GET \
--location 'http://localhost:8091/api/v1/customers?accountNumber=1000112' \
--header 'Content-Type: application/json'
```

---

#### Tentativa de transferencia que excede o valor maximo permitido de R$10.000,00

```shell
curl --request POST \
--location 'http://localhost:8091/api/v1/customers/3d05773e-513e-11ef-85b4-938a0beed59a/transfers' \
--header 'Content-Type: application/json' \
--data '{
    "agency": 1234,
    "accountNumber": 1000223,
    "amount": 10001
}'
```

---

#### Transferencia da Alice para conta do Bob

```shell
curl --request POST \
--location 'http://localhost:8091/api/v1/customers/3d05773e-513e-11ef-85b4-938a0beed59a/transfers' \
--header 'Content-Type: application/json' \
--data '{
    "agency": 1234,
    "accountNumber": 1000223,
    "amount": 1000
}'
```

#### 

```shell
curl --request GET \
--location 'http://localhost:8091/api/v1/customers/3d05773e-513e-11ef-85b4-938a0beed59a/transfers' \
--header 'Content-Type: application/json'
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.2/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>`
and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove
those overrides.

