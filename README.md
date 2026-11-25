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

## One image (page + API)

```bash
docker build -t flatledger:local .
docker run --rm -p 127.0.0.1:8095:8095 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/flatledger \
  -e SPRING_DATASOURCE_USERNAME=flatledger -e SPRING_DATASOURCE_PASSWORD=flatledger-local-only \
  flatledger:local              # page at http://127.0.0.1:8095, needs the compose database
```

## Deploy (Render + Neon, free tiers)

- **Database:** Neon project `flatledger`, region Singapore, Postgres 18. Flyway creates the tables on first start.
- **App:** Render web service from this repo, runtime Docker, region Singapore, auto-deploy after CI passes,
  health check `/actuator/health`. Environment variables (set in Render, never in the repo):
  `SPRING_DATASOURCE_URL` (`jdbc:postgresql://<neon-host>/flatledger?sslmode=require`),
  `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
- Free instances sleep after 15 idle minutes; the first visit afterwards takes about a minute.

## Rules

- Use `./gw`, not `backend/gradlew`: it pins Java 21 and keeps Gradle's files inside this folder.
- Use `./nw`, not plain `npm`: it pins Node 24 and keeps npm's cache inside this folder.
- Database changes only go through Flyway migrations in `backend/src/main/resources/db/migration/`.
- Never run `docker system prune` or `docker volume prune`: they delete other projects' Docker data too.
