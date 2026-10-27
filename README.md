## Integração com a AwesomeAPI

(Fazer depois...)

## Pré-requisitos

- JDK 21
- MySQL em execução no ambiente local
- Maven instalado, ou utilização do Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Acesso à internet para consultar a AwesomeAPI

## Configuração do banco de dados

Crie um banco MySQL para a aplicação e configure as credenciais no arquivo `src/main/resources/application.yaml`. 

Defina a url do banco, usuário e senha no ambiente antes de iniciar a aplicação, conforme a configuração do seu MySQL. Ajuste o nome do banco e as configurações conforme o ambiente local.

As migrations do Flyway devem criar as tabelas e inserir os dois clientes que serão gerados para teste.

### Clientes a serem gerados :

```
INSERT INTO tb_clientes (id, nome, email)
VALUES
    ('11111111-1111-4111-8111-111111111111',
     'Antonio', 'antonio@exemplo.com'),
    ('22222222-2222-4222-8222-222222222222',
     'Danilo', 'danilo@exemplo.com');
```

## Como executar

Na raiz do projeto:

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```bat
mvnw.cmd spring-boot:run
```

Alternativamente, com Maven instalado:

```bash
mvn spring-boot:run
```

Antes de executar, confirme que o banco de dados está acessível e que as variáveis no arquivo `src/main/resources/application.yaml` estão configuradas. O Flyway executará as migrations habilitadas na inicialização.
