package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.domain.CacheMetaData
import org.springframework.data.jpa.repository.JpaRepository

interface CacheMetaDataRepository : JpaRepository<CacheMetaData, String>
