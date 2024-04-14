# Sorface auth server

[![Java CI with Maven](https://github.com/sorface/sso-server/actions/workflows/maven.yml/badge.svg)](https://github.com/sorface/sso-server/actions/workflows/maven.yml)
![jacoco.svg](.github%2Fbadges%2Fjacoco.svg)

Система единого входа для платформы sorface.

С проектом можно ознакомится на странице https://sorface.com

## Docker запуск

Запусти команду из корневой папки проекта:

```shell
docker-compose -f docker/docker-compose.yaml up -d
```

```text
sh-3.2$ docker-compose -f docker/docker-compose.yaml up -d
[+] Running 4/5
 ⠧ Network docker_default            Created                                                                                                                                                                                                                                                              0.7s 
 ✔ Container redis                   Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container postgres                Started                                                                                                                                                                                                                                                              0.4s 
 ✔ Container sorface.security        Started                                                                                                                                                                                                                                                              0.5s 
 ✔ Container sorface.security.proxy  Started   
```

Перейди по ссылке из браузера:

```url
http://localhost:8080
```

Для управления конфигурацией проекта в docker:

- [.docker.backend.env](docker%2F.docker.backend.env) # конфигурация сервера аутентификации
- [.docker.redis.env](docker%2F.docker.redis.env) # конфигурация redis
- [.docker.pgsql.env](docker%2F.docker.pgsql.env) # конфигурация PostgreSQL

### Интеграции OAuth 2.0

- google
- yandex
- github
- twitch

Ознакомиться с конфигурацией можно в файле [application.yml](web%2Fsrc%2Fmain%2Fresources%2Fapplication.yml)

## OAuth2

Запрос аутентификации пользователя

```http request
GET http://localhost:8080/oauth2/authorize?
    client_id=************@sorface.oauth.client
    &scope=scope.read
    &response_type=code
    &redirect_uri=http%3A%2F%2Flocalhost%3A5043%2Foauth2%2Fsorface
    &
    state=CfDJ8Ivqng5udLJLqh1hbKpDiIsX2uiv8TgWsiV12hMtpTAZpHQc8xL0oSq6jErcYOMVQo0-wGR8rZdbc4A4vM5qiQZQcs2nujEZT-UqPl0E7f4nwzUjTDQC-L3x6g7JKR2BhiQKfw2_la6PxGRj4YsLHMYVctdXE7IqQEimF7sBSS7HkYMVleV3jEloVq21Za8IdinwlXii_kQNxcpnglI0wCxgbDxuG7ItIoWtZ0FP2JHe
```

```http request
GET http://localhost:5043/oauth2/sorface?code=XQSuU0_hxT1nomJq5IusEBW2Tv5-BPuMZycz-
```

```http request
POST http://localhost:8080/token?
    grant_type=authorization_code
    &client_id=************@sorface.oauth.client
    &client_secret=************
    &redirect_uri=https://www.oauth.com/playground/authorization-code.html
    &code=lbKAPTprbOBQ2Gfr0Xv6UTymRMBrC_-AONl2nwE0xrQx_Dyt
```

### Запрос данных по токену и принципалу

```http request
POST http://localhost:8080/oauth2/introspect

Authorization: Basic ************************ # clientId:secretId in base64
Content-Type: application/json

{
    "token": "************************" # access token
}
```

```json
{
  "active": true,
  "sub": "developerdevpav",
  "aud": [
    "GDChSngolAhWxgZgjyeBPDwOCKUbfKQKXtCAiNtFprAVFwhhymPrinY@sorface.oauth.client"
  ],
  "nbf": "2024-03-30T00:05:18.089765Z",
  "scopes": [
    "scope.read"
  ],
  "iss": "http://localhost:8080",
  "exp": "2024-03-30T00:35:18.089765Z",
  "iat": "2024-03-30T00:05:18.089765Z",
  "jti": "893112ae-29e6-4983-a05a-3e1b961a8efa",
  "clientId": "GDChSngolAhWxgZgjyeBPDwOCKUbfKQKXtCAiNtFprAVFwhhymPrinY@sorface.oauth.client",
  "tokenType": "Bearer",
  "principal": {
    "id": "50ad8927-0acc-4dcd-860b-2abdbad20394",
    "firstName": null,
    "lastName": null,
    "middleName": null,
    "birthday": null,
    "avatarUrl": "https://avatars.com/****.jpeg",
    "username": "developerdevpav",
    "email": "***************@yandex.ru",
    "authorities": []
  }
}
```

## OAuth2 создание приложений-клиентов пользователя

Пользователю доступно создание приложение-клинтов с сервером авторизации [sorface](https://sso.sorface.com)

Для создания клиента необходимо быть авторизированных пользователем.

Выполнить следующий запрос:

```http request
POST http://localhost:8080/api/applications
Cookie: JSESSIONID=************************
Content-Type: application/json

{
	"name": "myapp",
	"redirectionUrls": [
		"http://localhost:8080/callback"
	]
}
```

Ответ:

```json

{
  "id": "2d5db191-1edf-47aa-bb04-777123db0092", // идентификатор приложения
  "clientId": "YexwrISIWiVMJNvGsfHmXbHnpeVBdbqypUoHaVueIcFieUrArURowJk@sorface.oauth.client", // clientId приложения
  "clientSecret": "uwnwqCUzpynvRmPGbOytdDKZNjyOREoUeqjuapqNeCtNhMgfjklArfe", // clientSecret приложения (выдается только на время получения) дальше будет недоступен
  "clientName": "myapp", // название приложения
  "issueTime": "2024-03-31T12:13:27.273255", // дата запроса приложения
  "expiresAt": "2025-01-25T12:13:27.273296", // дата завершения действия clientSecret
  "redirectUrls": [ // OAuth Redirect URL кода будет перенаправлен запрос после аутентификации пользователя
    "http://localhost:8080/callback"
  ]
}
```

## Locale i18

Доступен выбор двух вариантов языков приложения

* Русский
* Английский

**По умолчанию используется английский язык (en-US)**

Язык определяется на основе переданного в Http Header Accept-Language:

```http request
GET ....
Accept-Language: ru-RU # русский
Accept-Language: en-US # английский
```

Влияет язык на:

* Возвращаемые ошибки (ru/en)
* На отправляемые email (ru/en)

На отдаваемые контент язык НЕ влияет

Заполняемые пользователем данные отдается в оригинальном виде
