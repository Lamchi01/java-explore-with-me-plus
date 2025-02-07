**Comments**
--

**Описание**:
Возможность добавления комментариев к событиям.

**Сервис**
main-service

На вход принимается тело запроса в формате JSON.

**Private Endpoints**:

    1. Добавление комментария для текущим пользователем:
    POST /comments/{eventId}/{userId}

    2. Удаление комментария текущим пользователем:
    DELETE /comments/{commentId}/{userId}

    3. Обновление комментария текущим пользователем:
    PATCH /comments/{commentId}/{userId}

    4. Получение комментариев к событию текущим пользователем:
    GET /comments/{eventId}/{userId}

    5. Получение всех комментариев текущим пользователем:
    GET /comments/users/{userId}

**Public Endpoints**:

    1. Получение комментариев к событию:
    GET /comments/events/{eventId}
    
    2. Получение комментария по его айди:
    GET /comments/{commentId}

**Admin Endpoints**:

    1. Удаление комментария
    DELETE /admin/comments/{commentId}

    2. Обновление любого комментария администратором:
    PATCH /admin/comments/{commentId}