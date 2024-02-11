package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmailAndTenantName(email: String, name: String): User?
}
