package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    val email: String,
    val password: String,
    val role: String,
    @ManyToOne
    val tenant: Tenant,
    @Id
    var id: Long? = null
)
