# Globetrotter Challenge - Backend

This is the backend for the **Globetrotter Challenge**, a full-stack geography-based quiz web app where users guess famous destinations based on clues. The backend is built with **Java Spring Boot**, secured via **JWT authentication**, and uses **PostgreSQL** as its database.

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **PostgreSQL**
- **Lombok**
- **Maven**

---

## 🧠 Core Features

### 🔐 Authentication Module (`/api/auth`)
- **User Registration**: Create a new user account with a username and password.
- **Login**: Authenticate and receive a JWT token for secure API access.
- **Logout**: Invalidate current session (dummy implementation placeholder).

### 🎮 Game Module (`/game`)
- **Start Game**: Start a new session by specifying user, number of questions, and time per question.
- **Get Next Question**: Fetch a random location-based question with four multiple-choice options.
- **Get Clue**: Get a clue about the destination. Multiple levels of hints available.
- **Submit Answer**: Submit your guess, evaluate correctness, and score based on time.
- **End Game**: Ends the session and saves the score.
- **Challenge a Friend**: Generate a challenge link/code to invite another user.
- **Accept Challenge**: Accept a pending challenge using a challenge code.
- **Game History**: Retrieve the last 5 played games for a user.

---

## 🛠️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/globetrotter-backend.git
cd globetrotter-backend

Configure PostgreSQL
Ensure PostgreSQL is installed and running. Create a database named globetrotter.

Environment Variables
Create an .env file (or configure your environment) with:

DB_USERNAME=your_pg_username
DB_PASSWORD=your_pg_password
