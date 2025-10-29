# NihongoMaster Backend

Simple Express backend that implements:

## Authentication APIs:
- POST /auth/register
- POST /auth/login
- POST /auth/refresh
- POST /auth/logout

## Reading Exercises APIs:
- GET /reading/articles - Lấy danh sách bài đọc
- GET /reading/articles/:id - Lấy chi tiết bài đọc + câu hỏi
- POST /reading/results - Submit kết quả làm bài
- GET /reading/results/:userId - Lấy lịch sử kết quả user

## Listening Exercises APIs:
- GET /listening/exercises - Lấy danh sách bài nghe
- GET /listening/exercises/:id - Lấy chi tiết bài nghe + transcript + câu hỏi
- POST /listening/results - Submit kết quả làm bài nghe
- GET /listening/results/:userId - Lấy lịch sử kết quả listening
- GET /listening/stats/:userId - Thống kê listening của user

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
