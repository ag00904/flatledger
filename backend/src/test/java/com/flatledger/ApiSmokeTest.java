package com.flatledger;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/** Starts the whole app and checks the two URLs T2 promises, plus what must stay hidden. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSmokeTest {

    @Autowired
    MockMvcTester mvc;

    @Test
    void pingReturnsOk() {
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
