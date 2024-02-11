package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.data.entity.Tenant
import org.springframework.data.jpa.repository.JpaRepository

interface TenantRepository : JpaRepository<Tenant, Long>
