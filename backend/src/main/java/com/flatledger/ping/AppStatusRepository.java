package com.flatledger.ping;

import org.springframework.data.jpa.repository.JpaRepository;

interface AppStatusRepository extends JpaRepository<AppStatus, Short> {}
