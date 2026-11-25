package com.flatledger;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

/**
 * Starts the app with the prod profile (database from Testcontainers instead of Neon)
 * and checks that the T7 production settings are the ones in effect.
 */
@SpringBootTest
@ActiveProfiles("prod")
@Import(TestcontainersConfiguration.class)
class ProdProfileTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    Environment env;

    @Test
    void poolIsSmallAndLetsNeonSuspend() {
        var hikari = (HikariDataSource) dataSource;
        assertThat(hikari.getMaximumPoolSize()).isEqualTo(5);
        assertThat(hikari.getMinimumIdle()).isZero();
        assertThat(hikari.getIdleTimeout()).isEqualTo(60_000);
    }

    @Test
    void trustsTheHostingProxyAndHidesHealthDetails() {
        assertThat(env.getProperty("server.forward-headers-strategy")).isEqualTo("framework");
        assertThat(env.getProperty("management.endpoint.health.show-details")).isEqualTo("never");
    }
}
