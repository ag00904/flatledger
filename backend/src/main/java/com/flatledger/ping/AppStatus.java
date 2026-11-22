package com.flatledger.ping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** The single row of app_status (migration V1). Read-only. */
@Entity
@Table(name = "app_status")
class AppStatus {

    static final short ID = 1;

    @Id
    private Short id;

    @Column(nullable = false, length = 20)
    private String status;

    protected AppStatus() {}

    String status() {
        return status;
    }
}
