package com.flatledger.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Smallest possible API call, used by the frontend (T4) and deploy checks (T7).
 * The answer comes from the database, so a working ping means the API can
 * reach Postgres and the migrations have run.
 */
@RestController
@RequestMapping("/api")
class PingController {

    record PingResponse(String status) {}

    private final AppStatusRepository appStatus;

    PingController(AppStatusRepository appStatus) {
        this.appStatus = appStatus;
    }

    @GetMapping("/ping")
    PingResponse ping() {
        var row = appStatus.findById(AppStatus.ID)
                .orElseThrow(() -> new IllegalStateException("app_status row missing: has migration V1 run?"));
        return new PingResponse(row.status());
    }
}
