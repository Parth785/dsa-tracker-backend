# DSA Tracker — Spring Boot Backend

## Tech Stack
- Java 17 + Spring Boot 3.2
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + PostgreSQL
- Lombok
- Railway deployment

---

## Project Structure

```
src/main/java/com/dsatracker/
├── DsaTrackerApplication.java
├── config/
│   └── SecurityConfig.java          # CORS + JWT filter chain
├── controller/
│   ├── AuthController.java          # POST /api/auth/register, /login
│   ├── ProblemController.java       # CRUD /api/problems
│   └── StreakController.java        # /api/streak/calendar, /mark, /stats
├── dto/
│   ├── AuthDto.java
│   ├── ProblemDto.java
│   └── StreakDto.java
├── entity/
│   ├── User.java
│   ├── Problem.java
│   └── StreakDay.java
├── repository/
│   ├── UserRepository.java
│   ├── ProblemRepository.java
│   └── StreakDayRepository.java
├── security/
│   ├── JwtUtils.java
│   ├── JwtAuthFilter.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── ProblemService.java
    └── StreakService.java
```

---

## Local Setup

### 1. Create PostgreSQL database
```sql
CREATE DATABASE dsa_tracker;
```

### 2. Set environment variables
```bash
cp .env.example .env
# Edit .env with your local DB credentials
```

### 3. Run
```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

---

## API Reference

### Auth
| Method | Endpoint | Body | Auth |
|--------|----------|------|------|
| POST | `/api/auth/register` | `{email, password}` | No |
| POST | `/api/auth/login` | `{email, password}` | No |

Both return: `{ token, email, userId }`

Use token as: `Authorization: Bearer <token>`

---

### Problems
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/problems` | All problems for logged-in user |
| GET | `/api/problems/{id}` | Single problem |
| POST | `/api/problems` | Create problem |
| PUT | `/api/problems/{id}` | Edit problem (partial update) |
| DELETE | `/api/problems/{id}` | Delete problem |
| GET | `/api/problems/patterns` | Pattern counts `[{pattern, count}]` |
| GET | `/api/problems/revision` | Problems needing revision |

**Create/Update body:**
```json
{
  "name": "Two Sum",
  "lcNumber": "1",
  "pattern": "Hashing",
  "difficulty": "Easy",
  "type": "new",
  "timeTaken": 15,
  "triggerNote": "Saw target sum → HashMap complement lookup",
  "mistakeNote": "Initially tried brute force O(n²)",
  "revisionStatus": "no",
  "date": "2025-01-15"
}
```

---

### Streak
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/streak/calendar` | Last 90 days + next 30 days |
| POST | `/api/streak/mark` | Mark a day done/skip/clear |
| GET | `/api/streak/current` | Current streak count |
| GET | `/api/streak/stats` | Full stats (streak + totals + patterns) |

**Mark body:**
```json
{ "date": "2025-01-15", "status": "done" }
```
Status options: `done` | `skip` | `clear`

---

## Deploy to Railway

### Step 1 — Push to GitHub
```bash
git init
git add .
git commit -m "initial commit"
git remote add origin https://github.com/yourusername/dsa-tracker-backend.git
git push -u origin main
```

### Step 2 — Create Railway project
1. Go to [railway.app](https://railway.app) → New Project
2. Deploy from GitHub repo → select your repo
3. Add a **PostgreSQL** service to the same project

### Step 3 — Set environment variables in Railway
Go to your service → Variables → Add:

| Key | Value |
|-----|-------|
| `DATABASE_URL` | Click "Add Reference" → select your PostgreSQL → `DATABASE_URL` |
| `DATABASE_USERNAME` | Reference → `PGUSER` |
| `DATABASE_PASSWORD` | Reference → `PGPASSWORD` |
| `JWT_SECRET` | Generate a long random string (32+ chars) |
| `CORS_ALLOWED_ORIGINS` | `https://your-app.vercel.app` |

### Step 4 — Deploy
Railway auto-deploys on every push to main. Check the build logs.
Your backend URL will be: `https://your-service-name.railway.app`

---

## After Backend is Live — Frontend env
In your React frontend `.env`:
```
VITE_API_URL=https://your-service-name.railway.app
```
