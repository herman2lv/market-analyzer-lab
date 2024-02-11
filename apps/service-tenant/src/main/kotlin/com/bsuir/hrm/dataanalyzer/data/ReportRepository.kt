package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.data.entity.Report
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository: JpaRepository<Report, Long>
