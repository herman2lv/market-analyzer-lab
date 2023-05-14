package com.bsuir.hrm.dataanalyzer.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "cache_meta_data")
data class CacheMetaData(
    @Id
    val category: String,
    val uploadTime: LocalDateTime,
)
