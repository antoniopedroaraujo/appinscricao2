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

## Integração externa -> AwesomeAPI

A aplicação utiliza a [AwesomeAPI](https://economia.awesomeapi.com.br/) para consultar a cotação atual do dólar americano em relação ao real brasileiro (USD-BRL).

Essa integração é realizada por meio do **Spring Cloud OpenFeign**, permitindo que a aplicação consulte a API externa de forma declarativa.

### Endpoint utilizado

```http
GET https://economia.awesomeapi.com.br/json/last/USD-BRL
```

A consulta retorna informações sobre a cotação mais recente do par USD-BRL.

### Dados utilizados

A aplicação utiliza os seguintes campos da resposta:

- `ask`: valor de venda do dólar, utilizado como cotação para os cálculos da compra.
- `create_date`: data e hora da cotação, utilizadas para registrar a referência temporal da cotação consultada.

A cotação recebida é convertida para `BigDecimal` para realizar os cálculos financeiros com precisão decimal.

### Utilização no cálculo da compra

A cotação consultada é utilizada para calcular o valor convertido de dólares para reais. Sobre esse valor são aplicadas a taxa de serviço e a tributação simulada, conforme as regras de negócio que foram estabelecidas.

A cotação utilizada e os valores calculados são armazenados no registro da compra, preservando o histórico da operação mesmo que a cotação do dólar mude posteriormente.

### Configuração e execução

A integração utiliza o endpoint público da AwesomeAPI.

Para executar a aplicação, é necessário somente ter acesso à internet e permitir que a aplicação faça requisições HTTPS ao domínio `economia.awesomeapi.com.br`.

### Tratamento de falhas

Caso a API externa esteja indisponível, ocorra um timeout ou a resposta não contenha uma cotação válida, a aplicação deverá tratar a falha adequadamente e impedir a persistência de uma compra sem uma cotação válida.

Erros de comunicação com o serviço externo são tratados sem expor detalhes internos da aplicação ao cliente da API.

### Referência

Documentação e serviço da AwesomeAPI: https://economia.awesomeapi.com.br/
