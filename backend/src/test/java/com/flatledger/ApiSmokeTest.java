package com.flatledger;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/**
 * Starts the whole app against a real Postgres (Testcontainers) and checks
 * the URLs M1 promises, the migration, and what must stay hidden.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class ApiSmokeTest {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    Flyway flyway;

    @Test
    void migrationV1IsApplied() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("1");
    }

    @Test
    void pingReturnsOkFromTheDatabase() {
        assertThat(mvc.get().uri("/api/ping"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("{\"status\":\"ok\"}");
    }

    @Test
    void healthIsUpWithoutDetails() {
        var health = assertThat(mvc.get().uri("/actuator/health")).hasStatusOk().bodyJson();
        health.extractingPath("$.status").isEqualTo("UP");
        // Outside the local profile, no component details (disk, database...) are shown.
        health.doesNotHavePath("$.components");
    }

    @Test
    void otherActuatorEndpointsAreNotExposed() {
        assertThat(mvc.get().uri("/actuator/env")).hasStatus(HttpStatus.NOT_FOUND);
        assertThat(mvc.get().uri("/actuator/beans")).hasStatus(HttpStatus.NOT_FOUND);
    }
}
