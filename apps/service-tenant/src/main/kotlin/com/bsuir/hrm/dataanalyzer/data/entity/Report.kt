package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "reports")
class Report(
    @ManyToOne
    val owner: User,
    val data: String,
    @Id
    var id: Long? = null
)
