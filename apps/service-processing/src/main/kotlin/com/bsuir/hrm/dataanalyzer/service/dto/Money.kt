package com.bsuir.hrm.dataanalyzer.service.dto

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency,
) {
    enum class Currency {
        BYN, RUB, USD, EUR,
    }
}
