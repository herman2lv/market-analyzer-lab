package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.data.UserRepository
import com.bsuir.hrm.dataanalyzer.data.entity.User
import com.bsuir.hrm.dataanalyzer.service.LoginService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(LoginServiceImpl::class.java)

@Service
class LoginServiceImpl(
    private val userRepository: UserRepository
) : LoginService {

    override fun login(tenant: String, email: String, password: String): User {
        log.trace("Invocation login")
        val user = userRepository.findByEmailAndTenantName(email, tenant) ?: throw SecurityException("Invalid email")
        if (user.password == password) {
            return user
        }
        throw SecurityException("Invalid password")
    }
}
