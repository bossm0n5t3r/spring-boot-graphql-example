# spring-boot-graphql-example

- `Kotlin`
- `Spring Boot`
- `JPA`
- `GraphQL`
  - `Netflix DGS Framework (Domain Graph Service)`

## cURL

```shell
curl --location --request POST 'localhost:8080/graphql' \
--header 'Content-Type: application/graphql' \
--data-raw 'query {
    getAllArticles {
        id,
        subject
    }
}'
```

## HTTPie

```shell
 http POST http://localhost:8080/graphql query="{getAllArticles{id,subject}}"
HTTP/1.1 200 
Connection: keep-alive
Content-Length: 120
Content-Type: application/json
Date: Sat, 26 Nov 2022 11:49:30 GMT
Keep-Alive: timeout=60

{
    "data": {
        "getAllArticles": [
            {
                "id": "1",
                "subject": "Hello"
            },
            {
                "id": "2",
                "subject": "Graphql"
            },
            {
                "id": "3",
                "subject": "Goodbye"
            }
        ]
    }
}
```
