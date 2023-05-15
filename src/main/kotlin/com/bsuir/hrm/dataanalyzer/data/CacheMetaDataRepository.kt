package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.data.entity.CacheMetaData
import org.springframework.data.jpa.repository.JpaRepository

interface CacheMetaDataRepository : JpaRepository<CacheMetaData, String>
