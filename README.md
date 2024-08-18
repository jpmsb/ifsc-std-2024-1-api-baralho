[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/g-3PKEPq)

# API RESTful para baralho de cartas

Neste projeto foi criada uma API RESTFul para a gerência de baralhos de cartas.

### Sumário

- [Requisitos atendidos](#requisitos-atendidos)
- [Desenho da API RESTful](#desenho-da-api-restful)
- [Execução do projeto](#execução-do-projeto)
- [Utilização da API](#utilização-da-api)
    - [Criação de um novo baralho](#criação-de-um-novo-baralho)
    - [Listagem de todos os baralhos criados](#listagem-de-todos-os-baralhos-criados)
    - [Listagem das cartas de um baralho](#listagem-das-cartas-de-um-baralho)
    - [Embalhar as cartas de um baralho](#embalhar-as-cartas-de-um-baralho)
    - [Listagem das cartas de um baralho já embaralhado](#listagem-das-cartas-de-um-baralho-já-embaralhado)
    - [Listagem de cartas de um baralho inexistente](#listagem-de-cartas-de-um-baralho-inexistente)
    - [Remoção de cartas de um baralho](#remoção-de-cartas-de-um-baralho)
    - [Remoção de cartas de um baralho inexistente](#remoção-de-cartas-de-um-baralho-inexistente)
    - [Remoção de cartas de um baralho já vazio](#remoção-de-cartas-de-um-baralho-já-vazio)
    - [Exclusão de um baralho](#exclusão-de-um-baralho)
    - [Exclusão de um baralho inexistente](#exclusão-de-um-baralho-inexistente)

## Requisitos atendidos

Todos os requisitos do projeto foram atendidos e são listados abaixo:

1. Utilizando o verbo POST na URL base (/baralhos), a API consegue criar um novo baralho de cartas com identificador não previsível e com todas as cartas sendo criadas sempre na mesma sequência;

1. Ao informar o identificador único do baralho na URL, utilizando o verbo GET, é possível listar todas as cartas ainda presentes no baralho, desde que o baralho ainda não tenha sido embaralhado. Além disso, no objeto JSON gerado, há um vetor de objetos JSON de cada carta, contendo o código único, o naipe, o valor e a URL onde é possível obter a imagem de cada carta;

1. Utilizando o verbo PUT na URL com o identificador único de um baralho, é possível embaralhar todas as cartas do mesmo. A resposta é um objeto JSON contendo a indicação de sucesso e a quantidade de cartas restantes;

1. É possível remover uma quantidade específicas de cartas do baralho utilizando o verbo PUT na URL com o identificador único do baralho, sendo que a quantidade de cartas a serem removidas deve ser informadas através de um objeto JSON com o parâmetro "quantidade". A resposta é um objeto JSON contendo um vetor de objetos com as cartas removidas, sendo cada objeto dentro desse vetor uma carta, semlhante ao que ocorre na listagem das cartas do baralho. Além disso, o objeto JSON retornado também contém a quantidade de cartas restantes no baralho. Caso seja informada uma quantidade maior do que a presente no baralho, todas as cartas restantes serão removidas;

1. Por fim, é possível excluir um baralho criado bastando utilizar o verbo DELETE na URL com o identificador único do baralho. Caso a operação seja bem sucedida, é retornado o código de status 204 (No Content), não havendo retorno de conteúdo. Entretanto, caso o baralho não seja encontrado, é retornado o código de status 404 (Not Found).

## Desenho da API RESTful

O quadro abaixo mostra a estrutura da API RESTful criada para o projeto:

| Verbo  | Recurso                 | Corpo do pedido | Corpo da resposta | HTTP Status |
|--------|-------------------------|-----------------|-------------------|-------------:|
| GET    | /baralhos               | -               | Documento JSON com os identificadores únicos dos baralhos criados | 200 |
| GET    | /baralhos/{uuid}        | -               | Documento JSON com as cartas do baralho | 200 ou 404 |
| GET    | /baralhos/carta/{carta} | -               | Imagem no formato PNG da carta solicitada. | 200 ou 404 |
| POST   | /baralhos               | -               | Documento JSON contendo o identificador único do baralho criado | 201 |
| PUT    | /baralhos/{uuid}        | -               | Documento JSON contendo a indicação de sucesso e a quantidade de cartas restantes | 200 |
| PUT    | /baralhos/{uuid}        | Documento JSON com a quantidade de cartas a serem removidas | Documento JSON contendo as cartas removidas e a quantidade de cartas restantes | 200, 400 ou 404 |
| DELETE | /baralhos/{uuid}        | -               | - | 204 ou 404 |


## Execução do projeto

Inicialmente, clone este repositório utilizando o comando abaixo:

```bash
git clone https://github.com/STD29006-classroom/2024-01-projeto-api-baralho-jpmsb
```

Em seguida, o projeto pode ser executado utilizando o comando abaixo:

```bash
./gradlew bootRun
```

Caso não ocorra erro, nenhuma mensagem deverá ser exibida no terminal, já que toda a saída de informação, que não seja erro, relacionada ao Gradle e ao SpringBoot foi silenciada.

## Utilização da API

As requisições na API podem ser realizadas utilizando o cliente HTTP em linha de comando `curl`. Abaixo, estão exemplos que mostram todas as capacidades da API:

### Criação de um novo baralho:

- Requisição:

    ```bash
    curl -sLX POST 'http://localhost:8080/baralhos/'
    ```

- Resposta:

    ```json
    {
        "uuid": "c02a8d9e-cc09-461a-b504-3c7cc2331046"
    }
    ```


### Listagem de todos os baralhos criados:

- Requisição:

    ```bash
    curl -sLX GET 'http://localhost:8080/baralhos/'
    ```

- Resposta:

    ```json
    {
        "baralhos": [
            "c02a8d9e-cc09-461a-b504-3c7cc2331046"
        ]
    }
    ```

### Listagem das cartas de um baralho:

- Requisição:

    ```bash
    curl -sLX GET 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046'
    ```

- Resposta:

    ```json
    {
        "cartas": [
            {
                "codigo": "1c",
                "naipe": "copas",
                "valor": "As",
                "url": "http://localhost:8080/baralhos/carta/1c.png"
            },
            {
                "codigo": "2c",
                "naipe": "copas",
                "valor": "2",
                "url": "http://localhost:8080/baralhos/carta/2c.png"
            },
            {
                "codigo": "3c",
                "naipe": "copas",
                "valor": "3",
                "url": "http://localhost:8080/baralhos/carta/3c.png"
            },
            {
                "codigo": "4c",
                "naipe": "copas",
                "valor": "4",
                "url": "http://localhost:8080/baralhos/carta/4c.png"
            },
            {
                "codigo": "5c",
                "naipe": "copas",
                "valor": "5",
                "url": "http://localhost:8080/baralhos/carta/5c.png"
            },
            {
                "codigo": "6c",
                "naipe": "copas",
                "valor": "6",
                "url": "http://localhost:8080/baralhos/carta/6c.png"
            },
            {
                "codigo": "7c",
                "naipe": "copas",
                "valor": "7",
                "url": "http://localhost:8080/baralhos/carta/7c.png"
            },
            {
                "codigo": "8c",
                "naipe": "copas",
                "valor": "8",
                "url": "http://localhost:8080/baralhos/carta/8c.png"
            },
            {
                "codigo": "9c",
                "naipe": "copas",
                "valor": "9",
                "url": "http://localhost:8080/baralhos/carta/9c.png"
            },
            {
                "codigo": "10c",
                "naipe": "copas",
                "valor": "10",
                "url": "http://localhost:8080/baralhos/carta/10c.png"
            },
            {
                "codigo": "jc",
                "naipe": "copas",
                "valor": "Valete",
                "url": "http://localhost:8080/baralhos/carta/jc.png"
            },
            {
                "codigo": "qc",
                "naipe": "copas",
                "valor": "Dama",
                "url": "http://localhost:8080/baralhos/carta/qc.png"
            },
            {
                "codigo": "kc",
                "naipe": "copas",
                "valor": "Rei",
                "url": "http://localhost:8080/baralhos/carta/kc.png"
            },
            {
                "codigo": "1e",
                "naipe": "espadas",
                "valor": "As",
                "url": "http://localhost:8080/baralhos/carta/1e.png"
            },
            {
                "codigo": "2e",
                "naipe": "espadas",
                "valor": "2",
                "url": "http://localhost:8080/baralhos/carta/2e.png"
            },
            {
                "codigo": "3e",
                "naipe": "espadas",
                "valor": "3",
                "url": "http://localhost:8080/baralhos/carta/3e.png"
            },
            {
                "codigo": "4e",
                "naipe": "espadas",
                "valor": "4",
                "url": "http://localhost:8080/baralhos/carta/4e.png"
            },
            {
                "codigo": "5e",
                "naipe": "espadas",
                "valor": "5",
                "url": "http://localhost:8080/baralhos/carta/5e.png"
            },
            {
                "codigo": "6e",
                "naipe": "espadas",
                "valor": "6",
                "url": "http://localhost:8080/baralhos/carta/6e.png"
            },
            {
                "codigo": "7e",
                "naipe": "espadas",
                "valor": "7",
                "url": "http://localhost:8080/baralhos/carta/7e.png"
            },
            {
                "codigo": "8e",
                "naipe": "espadas",
                "valor": "8",
                "url": "http://localhost:8080/baralhos/carta/8e.png"
            },
            {
                "codigo": "9e",
                "naipe": "espadas",
                "valor": "9",
                "url": "http://localhost:8080/baralhos/carta/9e.png"
            },
            {
                "codigo": "10e",
                "naipe": "espadas",
                "valor": "10",
                "url": "http://localhost:8080/baralhos/carta/10e.png"
            },
            {
                "codigo": "je",
                "naipe": "espadas",
                "valor": "Valete",
                "url": "http://localhost:8080/baralhos/carta/je.png"
            },
            {
                "codigo": "qe",
                "naipe": "espadas",
                "valor": "Dama",
                "url": "http://localhost:8080/baralhos/carta/qe.png"
            },
            {
                "codigo": "ke",
                "naipe": "espadas",
                "valor": "Rei",
                "url": "http://localhost:8080/baralhos/carta/ke.png"
            },
            {
                "codigo": "1o",
                "naipe": "ouros",
                "valor": "As",
                "url": "http://localhost:8080/baralhos/carta/1o.png"
            },
            {
                "codigo": "2o",
                "naipe": "ouros",
                "valor": "2",
                "url": "http://localhost:8080/baralhos/carta/2o.png"
            },
            {
                "codigo": "3o",
                "naipe": "ouros",
                "valor": "3",
                "url": "http://localhost:8080/baralhos/carta/3o.png"
            },
            {
                "codigo": "4o",
                "naipe": "ouros",
                "valor": "4",
                "url": "http://localhost:8080/baralhos/carta/4o.png"
            },
            {
                "codigo": "5o",
                "naipe": "ouros",
                "valor": "5",
                "url": "http://localhost:8080/baralhos/carta/5o.png"
            },
            {
                "codigo": "6o",
                "naipe": "ouros",
                "valor": "6",
                "url": "http://localhost:8080/baralhos/carta/6o.png"
            },
            {
                "codigo": "7o",
                "naipe": "ouros",
                "valor": "7",
                "url": "http://localhost:8080/baralhos/carta/7o.png"
            },
            {
                "codigo": "8o",
                "naipe": "ouros",
                "valor": "8",
                "url": "http://localhost:8080/baralhos/carta/8o.png"
            },
            {
                "codigo": "9o",
                "naipe": "ouros",
                "valor": "9",
                "url": "http://localhost:8080/baralhos/carta/9o.png"
            },
            {
                "codigo": "10o",
                "naipe": "ouros",
                "valor": "10",
                "url": "http://localhost:8080/baralhos/carta/10o.png"
            },
            {
                "codigo": "jo",
                "naipe": "ouros",
                "valor": "Valete",
                "url": "http://localhost:8080/baralhos/carta/jo.png"
            },
            {
                "codigo": "qo",
                "naipe": "ouros",
                "valor": "Dama",
                "url": "http://localhost:8080/baralhos/carta/qo.png"
            },
            {
                "codigo": "ko",
                "naipe": "ouros",
                "valor": "Rei",
                "url": "http://localhost:8080/baralhos/carta/ko.png"
            },
            {
                "codigo": "1p",
                "naipe": "paus",
                "valor": "As",
                "url": "http://localhost:8080/baralhos/carta/1p.png"
            },
            {
                "codigo": "2p",
                "naipe": "paus",
                "valor": "2",
                "url": "http://localhost:8080/baralhos/carta/2p.png"
            },
            {
                "codigo": "3p",
                "naipe": "paus",
                "valor": "3",
                "url": "http://localhost:8080/baralhos/carta/3p.png"
            },
            {
                "codigo": "4p",
                "naipe": "paus",
                "valor": "4",
                "url": "http://localhost:8080/baralhos/carta/4p.png"
            },
            {
                "codigo": "5p",
                "naipe": "paus",
                "valor": "5",
                "url": "http://localhost:8080/baralhos/carta/5p.png"
            },
            {
                "codigo": "6p",
                "naipe": "paus",
                "valor": "6",
                "url": "http://localhost:8080/baralhos/carta/6p.png"
            },
            {
                "codigo": "7p",
                "naipe": "paus",
                "valor": "7",
                "url": "http://localhost:8080/baralhos/carta/7p.png"
            },
            {
                "codigo": "8p",
                "naipe": "paus",
                "valor": "8",
                "url": "http://localhost:8080/baralhos/carta/8p.png"
            },
            {
                "codigo": "9p",
                "naipe": "paus",
                "valor": "9",
                "url": "http://localhost:8080/baralhos/carta/9p.png"
            },
            {
                "codigo": "10p",
                "naipe": "paus",
                "valor": "10",
                "url": "http://localhost:8080/baralhos/carta/10p.png"
            },
            {
                "codigo": "jp",
                "naipe": "paus",
                "valor": "Valete",
                "url": "http://localhost:8080/baralhos/carta/jp.png"
            },
            {
                "codigo": "qp",
                "naipe": "paus",
                "valor": "Dama",
                "url": "http://localhost:8080/baralhos/carta/qp.png"
            },
            {
                "codigo": "kp",
                "naipe": "paus",
                "valor": "Rei",
                "url": "http://localhost:8080/baralhos/carta/kp.png"
            }
        ]
    }
    ```
### Embalhar as cartas de um baralho:

- Requisição:

    ```bash
    curl -sLX PUT 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046'
    ```

- Resposta:

    ```json
    {
        "status": "sucesso",
        "quantidadeCartas": 52
    }
    ```

### Listagem das cartas de um baralho já embaralhado:

- Requisição:

    ```bash
    curl -sLX GET 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046'
    ```

- Resposta:

    ```json
    {
        "erro": "OOPS! O baralho com o identificador: 'c02a8d9e-cc09-461a-b504-3c7cc2331046' já está embaralhado!"
    }
    ```

### Listagem de cartas de um baralho inexistente:

- Requisição:

    ```bash
    curl -svLX GET 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc233104s'
    ```

- Resposta:

    ```
    * Host localhost:8080 was resolved.
    * IPv6: ::1
    * IPv4: 127.0.0.1
    *   Trying [::1]:8080...
    * Connected to localhost (::1) port 8080
    > GET /baralhos/c02a8d9e-cc09-461a-b504-3c7cc233104s HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/8.9.1
    > Accept: */*
    > 
    * Request completely sent off
    < HTTP/1.1 404 
    < Content-Type: text/plain;charset=UTF-8
    < Content-Length: 96
    < Date: Sat, 17 Aug 2024 18:55:34 GMT
    < 
    * Connection #0 to host localhost left intact
    Não foi possível encontrar o baralho com o identificador: c02a8d9e-cc09-461a-b504-3c7cc233104s
    ```

    Note que não há retorno de um documento JSON, porém há o status de código 404.

### Remoção de cartas de um baralho:

- Requisição:

    ```bash
    curl -sLX PUT 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046' -H 'Content-Type: application/json' -d '{"quantidade": 5}'
    ```

- Resposta:

    ```json
    {
        "quantidadeRestante": 47,
        "cartas": [
            {
                "codigo": "4o",
                "naipe": "ouros",
                "valor": "4",
                "url": "http://localhost:8080/baralhos/carta/4o.png"
            },
            {
                "codigo": "9p",
                "naipe": "paus",
                "valor": "9",
                "url": "http://localhost:8080/baralhos/carta/9p.png"
            },
            {
                "codigo": "10e",
                "naipe": "espadas",
                "valor": "10",
                "url": "http://localhost:8080/baralhos/carta/10e.png"
            },
            {
                "codigo": "6p",
                "naipe": "paus",
                "valor": "6",
                "url": "http://localhost:8080/baralhos/carta/6p.png"
            },
            {
                "codigo": "ko",
                "naipe": "ouros",
                "valor": "Rei",
                "url": "http://localhost:8080/baralhos/carta/ko.png"
            }
        ]
    }
    ```

    Na resposta acima, é possível notar que as cartas foram removidas de um baralho já embaralhado.

### Remoção de cartas de um baralho inexistente:

- Requisição:

    ```bash
    curl -vsLX PUT 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046d' -H 'Content-Type: application/json' -d '{"quantidade": 5}'
    ```

- Resposta:

    ```
    * Host localhost:8080 was resolved.
    * IPv6: ::1
    * IPv4: 127.0.0.1
    *   Trying [::1]:8080...
    * Connected to localhost (::1) port 8080
    > PUT /baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046d HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/8.9.1
    > Accept: */*
    > Content-Type: application/json
    > Content-Length: 17
    > 
    * upload completely sent off: 17 bytes
    < HTTP/1.1 404 
    < Content-Type: text/plain;charset=UTF-8
    < Content-Length: 97
    < Date: Sat, 17 Aug 2024 19:02:49 GMT
    < 
    * Connection #0 to host localhost left intact
    Não foi possível encontrar o baralho com o identificador: c02a8d9e-cc09-461a-b504-3c7cc2331046d
    ```

### Remoção de cartas de um baralho já vazio:

- Requisição:

    ```bash
    curl -sLX PUT 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046' -H 'Content-Type: application/json' -d '{"quantidade": 5}'
    ```

- Resposta:

    ```json
    {
        "erro": "OOPS! O baralho com o identificador: 'c02a8d9e-cc09-461a-b504-3c7cc2331046' está vazio!"
    }
    ```

### Exclusão de um baralho:

- Requisição:

    ```bash
    curl -vsLX DELETE 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046'
    ```

- Resposta:

    ```
    * Host localhost:8080 was resolved.
    * IPv6: ::1
    * IPv4: 127.0.0.1
    *   Trying [::1]:8080...
    * Connected to localhost (::1) port 8080
    > DELETE /baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/8.9.1
    > Accept: */*
    > 
    * Request completely sent off
    < HTTP/1.1 204 
    < Date: Sat, 17 Aug 2024 19:07:35 GMT
    < 
    * Connection #0 to host localhost left intact
    ```

### Exclusão de um baralho inexistente:

- Requisição:

    ```bash
    curl -vsLX DELETE 'http://localhost:8080/baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046'
    ```

- Resposta:

    ```
    * Host localhost:8080 was resolved.
    * IPv6: ::1
    * IPv4: 127.0.0.1
    *   Trying [::1]:8080...
    * Connected to localhost (::1) port 8080
    > DELETE /baralhos/c02a8d9e-cc09-461a-b504-3c7cc2331046 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/8.9.1
    > Accept: */*
    > 
    * Request completely sent off
    < HTTP/1.1 404 
    < Content-Type: text/plain;charset=UTF-8
    < Content-Length: 96
    < Date: Sat, 17 Aug 2024 19:08:43 GMT
    < 
    * Connection #0 to host localhost left intact
    Não foi possível encontrar o baralho com o identificador: c02a8d9e-cc09-461a-b504-3c7cc2331046
    ```