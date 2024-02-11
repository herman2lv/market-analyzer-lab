package com.bsuir.hrm.dataanalyzer.web

import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping

@Repository
class HomeController {

    @GetMapping("/")
    fun home(): String = "index"

}
