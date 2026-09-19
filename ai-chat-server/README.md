# AI Chat Server + Built-in Test UI

This project now includes a **browser UI for manual backend testing**.

The UI is a zero-build frontend (HTML/CSS/JavaScript) served directly by Spring Boot from:

- `src/main/resources/static/index.html`
- `src/main/resources/static/app.js`
- `src/main/resources/static/styles.css`

When the backend is running, open **http://localhost:8080/** to use it.

---

## What this UI can do

It is wired to your current backend endpoints:

| Action | Method | Endpoint |
|---|---|---|
| List conversations | GET | `/chat/conversations` |
| Get conversation history | GET | `/chat/conversation/{id}` |
| Create conversation | POST | `/chat/conversation/create` |
| Send message | POST | `/chat/conversation/{id}/sendMessage` |
| Delete conversation | DELETE | `/chat/conversation/{id}/delete` |

Features in UI:

1. Create a conversation by title.
2. Select an existing conversation.
3. Send messages and see AI replies.
4. Load existing message history.
5. Delete a conversation.

---

## Prerequisites

1. **Java 17+**
2. **PostgreSQL** running locally
3. (Optional) IDE like IntelliJ

---

## Database setup

Current backend config in `src/main/resources/application.properties` expects:

- DB: `ai_chat`
- User: `ai_chat_user`
- Password: `Chlprc1403`
- URL: `jdbc:postgresql://localhost:5432/ai_chat`

Create them in PostgreSQL, or edit `application.properties` to your own values.

Example SQL:

```sql
CREATE DATABASE ai_chat;
CREATE USER ai_chat_user WITH PASSWORD 'Chlprc1403';
GRANT ALL PRIVILEGES ON DATABASE ai_chat TO ai_chat_user;
```

---

## Run the backend (and UI)

From project root (`ai-chat-server` folder containing `pom.xml`):

```powershell
.\mvnw.cmd spring-boot:run
```

After startup:

- Backend API base: `http://localhost:8080/chat`
- UI: `http://localhost:8080/`

---

## How to use the UI for testing

1. Open `http://localhost:8080/`.
2. In the left panel, type a title and click **Create**.
3. Click the created conversation in the list.
4. Type a message in the input box and click **Send**.
5. Repeat to test multi-turn conversations.
6. Use **Delete** to remove a conversation and its messages.

---

## Notes and limitations

1. The message history endpoint currently returns only `{ id, text }` without sender type, so the UI infers roles as alternating `USER/AI` order.
2. No authentication is implemented (UI is intended for local testing).
3. Error messages from failed API calls are shown at the bottom of the page.

---

## Troubleshooting

### UI does not load

- Make sure Spring Boot started without errors.
- Confirm `http://localhost:8080/` is reachable.

### API/database errors in UI

- Check backend console logs.
- Verify PostgreSQL is running and credentials in `application.properties` are correct.

### Port already in use

- Stop the process using port `8080` or configure a different Spring port.
