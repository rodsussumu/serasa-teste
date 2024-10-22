# Teste Serasa Experian - Backend

## Tecnologias e bibliotecas utilizadas
* Java 17
* Spring Boot
* PostgreSQL
* Docker
* JUnit
* Mockito
* Jacoco
* Actuator
* Swagger

## Cobertura de testes
![Screenshot_130](https://github.com/user-attachments/assets/ce158cc4-cc3d-4e3b-af3c-b9b138309e64)

## Executando o projeto
* Execute docker-compose up --build`
* Ou possuir o PostgreSQL instalado na máquina e realizar a alteração das variaveis no application.properties

## Swagger/Actuator
* Swagger disponivel na url `http://localhost:8080/swagger-ui/index.html#/`
![Screenshot_128](https://github.com/user-attachments/assets/a5a7d376-aa0d-495a-bd10-efd2b5dea1f4)

* Actuator disponivel na url `http://localhost:8080/actuator`
![Screenshot_129](https://github.com/user-attachments/assets/594f49ed-4739-422f-8ec2-56cded48aa78)

## Detalhes e utilizações
* Ao executar a aplicação, são criados 2 usuarios.
```
{
  "username": "admin",
  "password": "1234"
}
```
e
```
{
  "username": "user",
  "password": "1234"
}
```
* É possivel fazer a autenticação na rota /auth/login e gerar o token que será utilizado para as outras chamadas existentes
* O usuário "admin" possui permissão para todas as rotas
* O usuário "user" possui permissão apenas para as rotas GET(/author e /book)
