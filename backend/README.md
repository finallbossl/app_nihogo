# NihongoMaster Auth Backend

Simple Express backend that implements:
- POST /auth/register
- POST /auth/login
- POST /auth/refresh
- POST /auth/logout

Uses lowdb for simple JSON persistence (`backend/db.json`).

How to run:

```powershell
cd backend
npm install
npm start
```

Default port: 4000

Responses follow the DTO structure expected by the Android client:
- Auth response: { accessToken, refreshToken, expiresIn, userId, email }

Notes:
- This is a minimal dev server. For production, replace lowdb with a real DB and store secrets in env vars.
