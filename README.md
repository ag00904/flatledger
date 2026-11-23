# FlatLedger

Shared household expense tracking (INR) for flats, PGs and hostels.
`backend/`: Java 21 + Spring Boot API. `frontend/`: React + TypeScript (Vite).

## Prerequisites

- Java 21 installed (it does not need to be your default Java)
- Docker Desktop
- Node 24 via nvm (`nvm use` in this folder)

## First-time setup after cloning

```bash
git config --local user.name "Your Name"
git config --local user.email "you@example.com"
git config --local commit.template "$PWD/.gitmessage"
```

## Run

```bash
docker compose up -d --wait   # local Postgres on 127.0.0.1:5433
./gw test                     # backend tests (needs Docker running)
./gw bootRun                  # API on http://localhost:8095
./nw ci                       # first time: install frontend packages
./nw run dev                  # web app on http://127.0.0.1:5175 (calls the API)
./nw test                     # frontend tests
```

Check it: `curl localhost:8095/api/ping`. Stop the database with `docker compose stop`.

## Rules

- Use `./gw`, not `backend/gradlew`: it pins Java 21 and keeps Gradle's files inside this folder.
- Use `./nw`, not plain `npm`: it pins Node 24 and keeps npm's cache inside this folder.
- Database changes only go through Flyway migrations in `backend/src/main/resources/db/migration/`.
- Never run `docker system prune` or `docker volume prune`: they delete other projects' Docker data too.
