package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tenants")
class Tenant(
    @Column(unique = true)
    val name: String,
    @Id
    var id: Long? = null
)
