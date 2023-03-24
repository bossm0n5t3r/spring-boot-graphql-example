# spring-boot-graphql-example

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.4-brightgreen)

- `Kotlin`
- `Spring Boot`
  - `Spring for GraphQL`
- `JPA`

- [https://docs.spring.io/spring-graphql/docs/current/reference/html/](https://docs.spring.io/spring-graphql/docs/current/reference/html/)

## HTTPie

```shell
 http POST http://localhost:8080/graphql query="{allArticles{id subject content}}"
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 28 Nov 2022 16:30:35 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "data": {
        "allArticles": [
            {
                "content": "world!",
                "id": "1",
                "subject": "Hello"
            },
            {
                "content": "learning",
                "id": "2",
                "subject": "Graphql"
            }
        ]
    }
}

 http POST http://localhost:8080/graphql query="{article(articleId: 1){id subject content}}"
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 28 Nov 2022 16:37:40 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "data": {
        "article": {
            "content": "world!",
            "id": "1",
            "subject": "Hello"
        }
    }
}

 http POST http://localhost:8080/graphql query="mutation {createArticle(dto: {subject: \"TEST SUBJECT\", content: \"TEST CONTENT\"}){id subject content}}"
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 28 Nov 2022 16:47:30 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "data": {
        "createArticle": {
            "content": "TEST CONTENT",
            "id": "5",
            "subject": "TEST SUBJECT"
        }
    }
}

 http POST http://localhost:8080/graphql query="mutation {updateArticle(articleId: 5, dto: {subject: \"TEST SUBJECT ADDITIONAL\", content: \"TEST CONTENT ADDITIONAL\"}){id subject content}}"
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 28 Nov 2022 16:49:05 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "data": {
        "updateArticle": {
            "content": "TEST CONTENT ADDITIONAL",
            "id": "5",
            "subject": "TEST SUBJECT ADDITIONAL"
        }
    }
}

 http POST http://localhost:8080/graphql query="mutation {deleteArticle(articleId: 5){id subject content}}" 
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Mon, 28 Nov 2022 16:50:00 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "data": {
        "deleteArticle": {
            "content": "TEST CONTENT ADDITIONAL",
            "id": "5",
            "subject": "TEST SUBJECT ADDITIONAL"
        }
    }
}
```
