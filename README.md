# Notes Service

REST API сервиса управления заметками на Spring Boot 3 и Java 17.

## Возможности

- CRUD для заметок.
- Фильтрация заметок по тегу.
- H2 in-memory storage.
- Валидация входных данных.
- Unit-тесты сервисного слоя на JUnit 5 и Mockito.
- Docker multi-stage build и запуск через docker-compose.

## Запуск локально

```powershell
.\mvnw.cmd spring-boot:run
```

API будет доступен на `http://localhost:8080`.

## Запуск в Docker

```powershell
docker compose up --build
```

## Тесты

```powershell
.\mvnw.cmd test
```

## REST API

### Создать заметку

```http
POST /api/notes
Content-Type: application/json

{
  "title": "First note",
  "content": "Note content",
  "tags": ["work", "java"]
}
```

Успешный ответ: `201 Created`.

### Получить все заметки

```http
GET /api/notes
```

### Фильтр по тегу

```http
GET /api/notes?tag=work
```

### Получить заметку по id

```http
GET /api/notes/1
```

Если заметка не найдена: `404 Not Found`.

### Обновить заметку

```http
PUT /api/notes/1
Content-Type: application/json

{
  "title": "Updated note",
  "content": "Updated content",
  "tags": ["work", "spring"]
}
```

### Удалить заметку

```http
DELETE /api/notes/1
```

Успешный ответ: `204 No Content`.

## Пример curl

```powershell
curl -Method POST http://localhost:8080/api/notes `
  -ContentType "application/json" `
  -Body '{"title":"First note","content":"Note content","tags":["work","java"]}'
```
