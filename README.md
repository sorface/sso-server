# Sorface SSO server

Система единого входа для платформы sorface.

С проектом можно ознакомится на странице https://sorface.com

### Интеграции OAuth 2.0

- google
- yandex
- github
- twitch

Ознакомиться с конфигурацией можно в файле [application.yml](web%2Fsrc%2Fmain%2Fresources%2Fapplication.yml)

Главная страница входа

![login-page.png](imgs%2Flogin-page.png)

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

Пример:

Accept-Language en-US:

![locale.validate-error-en.png](imgs%2Flocale.validate-error-en.png)

Accept-Language ru-RU:

![locale.validate-error-ru.png](imgs%2Flocale.validate-error-ru.png)