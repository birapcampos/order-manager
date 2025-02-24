# API - Gestão de Pedidos

## ESCOPO DO PROJETO

Criar um serviço "order", que vai gerenciar e calcular os produtos do pedido, sendo que os pedidos serão disponibilizados 
por outro sistema externo A, e após nossa gestão dos pedidos realizada no serviço "order", 
devemos disponibilizar a outro sistema externo B.

Precisamos disponibilizar uma integração com o sistema externo A para o recebimento dos pedidos, e calcular no 
serviço "order" o total dos produtos, somando o valor de cada produto dentro do pedido,e será necessário 
disponibilizar uma consulta dos pedidos e produtos relacionados, junto com seu status para 
o sistema externo B receber os pedidos calculados.

## Premissas e Regras de negócio

Para o desenvolvimento da solução, com base no escopo fornecido, algumas premissas e regras de negócio foram adotadas.

- Foi adotado o banco noSQL MongoDB (em um container docker)
- O processo foi implementado de forma Síncrona
- Os atributos para o "cabeçalho" do pedido são: data (orderDate) e customerId (Cliente)
- Os atributos para lista de itens são (productId, productPrice e quantity)
- Está sendo considerado um pedido por requisição na criação de um pedido (1-N) um pedido N Itens
- O id do pedido (orderId) está sendo gerado automaticamente pela API
- O customerId não esta sendo validado quanto a existência na base de dados
- Foram implementadas as seguintes validações:
  - orderDate e customerId do cabeçalho do pedido são obrigatórios na requisição
  - O productId é obrigatório na requisição
  - O productPrice e quantity devem ser maiores do que zero
  - O productId do produto informado na requisição deve existir na base de dados
  - O pedido é considerado duplicado quando existem produtos com o mesmo productId na lista (orderItens)
- Foi implementado um CRUD básico para produtos
- O valor total de cada pedido é calculado no momento da criação
- O calculo esta sendo feito pela soma da multiplicação de productPrice * quantity de cada item
  - productPrice esta sendo considerado o preço unitário do produto
- O único status que está sendo tratado é COMPLETE, que indica que o pedido foi calculado e inserido na base com sucesso.

## Tecnologias Utilizadas

- **Spring Boot**: Framework principal para a construção do aplicativo.
- **Spring Web**: Para construção de aplicações web.
- **Spring Data MongoDB**: Para manipulação das collections do MongoDB
- **Lombok**: Para reduzir o código boilerplate.
- **MongoDB**: (Rodando em um container Docker)
- **Maven**: Gerenciador de dependências e ferramenta de construção do projeto.
- **OpenAPI(Swagger)**: Para documentação da API.
- **Spring Boot Starter Test**: Para os testes unitários.
- **Docker**: Permite criar, executar e gerenciar aplicativos em contêineres

## Pré-requisitos

- Java 17 ou superior
- Maven
- Docker instalado no ambiente local e funcionando
   ```
      //Versão do docker que estou usando
      docker --version
      Docker version 27.4.0, build bde2b89
   ```

#### Observação sobre o uso do docker
Caso não queira utilizar o docker, você pode apontar para qualquer outro
servidor mongo, como por exemplo, MongoDB Atlas. Para isso basta configurar
a conexão com o mongoDB no application.yml do projeto.

application.yml atual
```
    server:
      port: 8085
    spring:
      application:
        name: orders-management
      data:
        mongodb:
          hast: localhost
          port: 27017
          authentication-database: admin
          username: root
          password: example
          database: orderDB
    springdoc:
      api-docs:
        enabled: true
      swagger-ui:
        enabled: true
```
Exemplo de application.yml com MondoDB Atlas
```
  server:
    port: 8085
  spring:
    application:
      name: orders-management
  data:
    mongodb:
      uri: mongodb+srv://<username>:<password>@<cluster-address>/<database>?retryWrites=true&w=majority

springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true

```
## Principais Endpoints de produtos

#### 1. **POST api/v1/product**
- **Descrição**: Cria um novo produto.
- **Parâmetros**: Requer um corpo de solicitação com as informações do produto.
- **Resposta**:
    - **201**: Pedido criado com sucesso.
    - **400**: Dados inválidos.
    - **500**: Erro interno no servidor.
- Exemplo de URL : http://localhost:8085/api/v1/product
  - Body (Json)
    ```
    {
      "name": "Guaraná Antarctica Garrafa 2l",
      "price": 4.5
    }
  ```
#### 2. **GET api/v1/product**
- **Descrição**: Lista os produtos cadastrados
- **Resposta**:
    - **200**: Produtos encontrados com sucesso.
    - **500**: Erro interno no servidor.
- Exemplo de URL : http://localhost:8085/api/v1/product
  - Response:
    ```
    [
    {
    "id": "6783b07f3ed59806ee043e15",
    "name": "Cerveja Brahma 350 ml 12 unidades",
    "price": 25.0
    },
    {
    "id": "6783b4ffaa58ec1e15164ec7",
    "name": "Cerveja Budweiser American Lager Lata 350ml com 12 unidades",
    "price": 30.0
    },
    {
    "id": "6783be2e624bf449b95db2d5",
    "name": "Cerveja Corona Extra Long Neck 330ml com 12 unidades",
    "price": 48.5
    },
    {
    "id": "6783e2c56e13101e21600546",
    "name": "Cerveja Duplo Malte Puro Malte Brahma Lata 350ml com 12 unidades",
    "price": 25.0
    },
    {
    "id": "6783f87de98bac63782e4c36",
    "name": "Guaraná Antarctica Garrafa 2l",
    "price": 4.5
    }
    ]
    ```
#### 2. **GET api/v1/product/{productId}**
- **Descrição**: Obtem um produto pelo id
- **Resposta**:
  - **200**: Produto encontrado com sucesso.
  - **500**: Erro interno no servidor.
- Exemplo de URL : http://localhost:8085/api/v1/product/6783b07f3ed59806ee043e15
  - Response:
    ```
     {
       "id": "6783b07f3ed59806ee043e15",
       "name": "Cerveja Brahma 350 ml 12 unidades",
       "price": 25.0
     }
    ```

## Endpoints de pedidos (Orders)

#### 1. **POST api/v1/order**
- **Descrição**: Cria um novo pedido.
- **Parâmetros**: Requer um corpo de solicitação com as informações do pedido (dados de `orderRequest`).
- **Resposta**:
    - **201**: Pedido criado com sucesso.
    - **400**: Dados inválidos.
    - **500**: Erro interno no servidor.
- Exemplo de URL : http://localhost:8085/api/v1/order
- Body (Json)
  ```
  {
    "customerId": 874,
    "orderDate": "2025-01-12",
    "items": [
      {
      "productId": "6783b07f3ed59806ee043e15",
      "productPrice": 22.90,
      "quantity": 500
      },
      {
      "productId": "6783be2e624bf449b95db2d5",
      "productPrice": 27.8,
      "quantity": 350
      },
      {
      "productId": "6783f87de98bac63782e4c36",
      "productPrice": 3.7,
      "quantity": 350
      }
    ]
  }
  ```

#### 2. **GET api/v1/order**
- **Descrição**: Lista todos os pedidos, com filtros de data e status.
- **Parâmetros**:
    - `orderDate`: Data dos pedidos (obrigatório).
    - `status`: Status dos pedidos (opcional).
- **Resposta**:
    - **200**: Pedidos encontrados com sucesso.
    - **500**: Erro interno no servidor.
  
- Exemplo de URL : http://localhost:8085/api/v1/order?orderDate=2025-01-12&status=COMPLETE
- Body (Json)
  ```
  [
	{
		"orderId": "6783e586106be90eeaf417f2",
		"orderDate": "2025-01-12",
		"status": "COMPLETE",
		"customerId": 874,
		"orderAmount": 17381.0,
		"items": [
			{
				"productId": "6783b07f3ed59806ee043e15",
				"productPrice": 22.9,
				"quantity": 50
			},
			{
				"productId": "6783be2e624bf449b95db2d5",
				"productPrice": 27.8,
				"quantity": 250
			},
			{
				"productId": "6783b4ffaa58ec1e15164ec7",
				"productPrice": 27.8,
				"quantity": 120
			},
			{
				"productId": "6783e2c56e13101e21600546",
				"productPrice": 23.8,
				"quantity": 250
			}
		]
	}
  ]
  ```

#### 3. **GET api/v1/order/summary**
- **Descrição**: Retorna um resumo dos pedidos com base em data e status.
- **Parâmetros**:
    - `orderDate`: Data dos pedidos (obrigatório).
    - `status`: Status dos pedidos (opcional).
- **Resposta**:
    - **200**: Resumo dos pedidos encontrado com sucesso.
    - **500**: Erro interno no servidor.
  
- Exemplo de URL : http://localhost:8085/api/v1/order/summary?orderDate=2025-01-12&status=COMPLETE
- Body (Json)
  ```
    {
    "orders": [
    {
    "orderId": "6783e586106be90eeaf417f2",
    "orderDate": "2025-01-12",
    "status": "COMPLETE",
    "customerId": 874,
    "orderAmount": 17381.0
    },
    {
    "orderId": "6783efd183732a681bf426f4",
    "orderDate": "2025-01-12",
    "status": "COMPLETE",
    "customerId": 874,
    "orderAmount": 21180.0
    },
    {
    "orderId": "6783f8a6e98bac63782e4c37",
    "orderDate": "2025-01-12",
    "status": "COMPLETE",
    "customerId": 874,
    "orderAmount": 22475.0
    }
    ],
    "totalAmount": 61036.0
    }
  ```

#### 4. **GET api/v1/order/{orderId}**
- **Descrição**: Retorna os detalhes de um pedido específico, pelo `orderId`.
- **Parâmetros**:
    - `orderId`: ID do pedido.
- **Resposta**:
    - **200**: Pedido encontrado com sucesso.
    - **404**: Pedido não encontrado.
    - **500**: Erro interno no servidor.
### Respostas para os Endpoints

- **201**: Pedido criado com sucesso.
- **400**: Dados inválidos.
- **500**: Erro interno no servidor.

- Exemplo de URL : http://localhost:8085/api/v1/order/6783e586106be90eeaf417f2
- Body (Json)
  ```
   {
	"orderId": "6783e586106be90eeaf417f2",
	"orderDate": "2025-01-12",
	"status": "COMPLETE",
	"customerId": 874,
	"orderAmount": 17381.0,
	"items": [
		{
			"productId": "6783b07f3ed59806ee043e15",
			"productPrice": 22.9,
			"quantity": 50
		},
		{
			"productId": "6783be2e624bf449b95db2d5",
			"productPrice": 27.8,
			"quantity": 250
		},
		{
			"productId": "6783b4ffaa58ec1e15164ec7",
			"productPrice": 27.8,
			"quantity": 120
		},
		{
			"productId": "6783e2c56e13101e21600546",
			"productPrice": 23.8,
			"quantity": 250
		}
    ]
    }
  ```
  
### Passos para Executar o projeto

1. Clone o repositório:
```
cd seu-repositorio
git clone https://github.com/birapcampos/order-manager.git
```

2.Rodando o projeto (API)

- Acesse o terminal
- Navegue até a pasta docker-local na raiz do projeto onde esta o arquivo: docker-compose.yml

    - O docker-compose.yml irá baixar as imagens e criar o container docker para o mongoDB e mongo-express
    - Execute os comandos abaixo:
      ```
        // Baixa as imagens e cria o container para o MongoDB e MongoDB Express)
        docker-compose up -d 
        // Verifica se o container foi criado e esta rodando
        docker ps
      ```

3.Compilação e Execução:
```
mvn clean install
mvn spring-boot:run  
```

- Se preferir pode rodar o projeto direto na IDE de sua preferencia (foi utilizado o IntelliJ IDEA)
- Após a aplicação estar no ar:
  - A API esta configurada para rodar na porte 8085 (http://localhost:8085)
  - Você pode utilizar o Swagger para testar os endpoints usando a URL abaixo:
  - http://localhost:8085/swagger-ui/index.html#/
  - Se preferir pode utilizar o Postman ou Insomnia para fazer os testes.


## Considerações Gerais


### Solução Atual 
Atualmente a solução implementada é síncrona, onde o Sistema A envia uma solicitação para a inclusão de um pedido e
imediatamente recebe uma resposta, com o pedido sendo processado e calculado no mesmo momento. 
O Sistema B, por sua vez, acessa os endpoints de consulta para obter os pedidos calculados, garantindo que o 
processo de cálculo e disponibilização dos pedidos seja realizado de forma sequencial e sincronizada.

### Alternativa de Solução Assíncrona

Um alternativa assíncrona pode ser implementada para melhorar a escalabilidade e eficiência do sistema, 
principalmente em cenários de alta carga ou onde os tempos de processamento podem ser mais longos. Nessa abordagem, 
o fluxo seria alterado da seguinte forma:

  - Sistema A envia a solicitação de inclusão do pedido, mas, ao invés de aguardar uma resposta imediata, recebe 
    Uma confirmação de que a solicitação foi recebida e que o processamento está em andamento.

  - O processamento do pedido ocorre de forma assíncrona, em uma fila de mensagens ou processo em segundo plano, 
    esse processamento pode incluir a validação dos produtos, o cálculo dos valores e a atualização do status do pedido.

  - O Sistema B, ao invés de realizar consultas periódicas para verificar se os pedidos estão calculados, escuta um tópico 
    no Apache Kafka ou outro sistema de mensageria. Quando o pedido é processado e o cálculo é concluído, um evento é enviado 
    para o tópico, notificando o Sistema B de que os pedidos estão prontos para serem acessados.

### Benefícios da Solução Assíncrona:

  - Escalabilidade: O processamento assíncrono permite que o sistema lide com uma carga maior, 
    processando múltiplos pedidos simultaneamente sem bloquear a resposta do sistema.

  - Desempenho: Ao não bloquear a resposta imediata do Sistema A, ele pode continuar com outras operações, 
    enquanto o pedido é processado em segundo plano.

  - Notificação eficiente: O Sistema B pode ser notificado automaticamente por meio do Apache Kafka
    ou outro sistema de mensageria, evitando a necessidade de consultas repetitivas e garantindo 
    que o Sistema B só será notificado quando os pedidos estiverem prontos.
