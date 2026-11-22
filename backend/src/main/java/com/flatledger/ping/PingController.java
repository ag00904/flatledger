package com.flatledger.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Smallest possible API call, used by the frontend (T4) and deploy checks (T7). */
@RestController
@RequestMapping("/api")
class PingController {

    record PingResponse(String status) {}

    @GetMapping("/ping")
    PingResponse ping() {
        return new PingResponse("ok");
    }
}
