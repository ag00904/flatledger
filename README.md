# FlatLedger

Shared household expense tracking (INR) for flats, PGs and hostels.

## Layout

| Folder | What |
|---|---|
| `backend/` | Java 21 + Spring Boot 4.2 API, built with Gradle 9.9 |
| `frontend/` | React + TypeScript + Vite (added in T4) |

## Prerequisites

- **Java 21** installed (not necessarily the default). `./gw` selects it for its own command only (macOS: found via `/usr/libexec/java_home -v 21`).
- **Node 24** via nvm: run `nvm use` in this folder (reads `.nvmrc`).
- **Docker** for the local database (T3).

## First-time setup after cloning

Repo-local Git settings are not part of a clone, so set them once:

```bash
git config --local user.name "Your Name"
git config --local user.email "you@example.com"
git config --local commit.template "$PWD/.gitmessage"
```

## Backend commands

Always use `./gw` (from the repo root), never `backend/gradlew` directly:

| Command | What it does |
|---|---|
| `./gw test` | Runs the backend tests |
| `./gw bootRun` | Starts the API on http://localhost:8095 (`local` profile) |
| `./gw --stop` | Stops FlatLedger's own Gradle daemon (not other projects') |

Check it is up: `curl localhost:8095/api/ping` and `curl localhost:8095/actuator/health`.

`./gw` refuses to run if `SPRING_*`, `SERVER_*`, `MANAGEMENT_*`, `ORG_GRADLE_*`, `JAVA_TOOL_OPTIONS`, `_JAVA_OPTIONS`, `JDK_JAVA_OPTIONS`, `JAVA_OPTS` or `GRADLE_OPTS` is set in your shell, so settings from other projects cannot leak in. Set `FLATLEDGER_ALLOW_ENV=1` to override on purpose.

## Local ports

| What | Port |
|---|---|
| Backend API | 8095 |
| Frontend dev server (T4) | 5175 |
| Postgres in Docker (T3) | 5433 |
| Java debugger, if used | 5006 |

## Environment isolation

Everything FlatLedger needs is pinned inside this folder:

- Java version: Gradle toolchain + daemon JVM criteria in `backend/`, JDK auto-download off.
- Gradle home: `.gradle-home/` in this folder (gitignored); `~/.gradle` is not used.
- Node version: `.nvmrc`; nvm's global default is not changed.
- Git identity and commit template: repo-local `.git/config`; global Git config is not changed.

No shell profile, global PATH or system default is modified.
