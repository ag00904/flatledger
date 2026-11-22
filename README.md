# FlatLedger

Shared household expense tracking (INR) for flats, PGs and hostels.

## Layout

| Folder | What |
|---|---|
| `backend/` | Java 21 + Spring Boot API (added in T2) |
| `frontend/` | React + TypeScript + Vite (added in T4) |

## Prerequisites

- **Java 21** installed (not necessarily the default). The backend launcher `./gw` (T2) selects it for its own command only.
- **Node 24** via nvm: run `nvm use` in this folder (reads `.nvmrc`).
- **Docker** for the local database (T3).

## Environment isolation

Everything FlatLedger needs is pinned inside this folder:

- Java version: Gradle toolchain + daemon JVM criteria in `backend/`, JDK auto-download off.
- Gradle home: `.gradle-home/` in this folder (gitignored); `~/.gradle` is not used.
- Node version: `.nvmrc`; nvm's global default is not changed.
- Git identity: repo-local `.git/config`; global Git config is not changed.

No shell profile, global PATH or system default is modified.
