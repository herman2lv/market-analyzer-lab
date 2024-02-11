package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.data.entity.User

interface LoginService {

    fun login(tenant: String, email: String, password: String): User

}
