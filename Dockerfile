# syntax=docker/dockerfile:1
# FlatLedger as one image (T5): Spring Boot serves the built React page at / and the API at /api.
# Build:  docker build -t flatledger:local .
# Settings come from environment variables at run time; nothing secret is in the image.

# --- Stage 1: build the page (thrown away after the build) ---
FROM node:24.22.3-alpine AS web
WORKDIR /src/frontend
COPY frontend/package.json frontend/package-lock.json frontend/.npmrc ./
# npm's download cache lives in Docker's build cache, not on the host.
RUN --mount=type=cache,target=/root/.npm npm ci --no-fund --no-audit
COPY frontend/ ./
RUN npm run build

# --- Stage 2: build the jar (thrown away after the build) ---
# The official Gradle image has the same Gradle 9.9.2 as our wrapper, so nothing is downloaded
# from GitHub (that host times out from Docker on this Mac; see T5 results in the task doc).
FROM gradle:9.9.2-jdk21-alpine AS app
WORKDIR /src/backend
COPY backend/settings.gradle.kts backend/build.gradle.kts backend/gradle.properties ./
COPY backend/gradle/gradle-daemon-jvm.properties ./gradle/
# Download dependencies first, so code changes do not repeat this step.
# Gradle's cache lives in Docker's build cache, not on the host.
RUN --mount=type=cache,target=/home/gradle/.gradle gradle --no-daemon dependencies > /dev/null
COPY backend/src/main ./src/main
COPY --from=web /src/frontend/dist ./src/main/resources/static
# T5-e: tests run in ./gw test and CI, not here.
RUN --mount=type=cache,target=/home/gradle/.gradle gradle --no-daemon bootJar \
 && cp build/libs/flatledger-0.0.1-SNAPSHOT.jar /app.jar

# --- Stage 3: what runs (only Java and the jar) ---
FROM eclipse-temurin:24.0.2_12-jre-alpine
RUN addgroup -S flatledger && adduser -S -G flatledger flatledger
WORKDIR /app
COPY --from=app /app.jar app.jar
USER flatledger
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8095
# Use at most 75% of the container's memory for the Java heap (small free hosts).
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
