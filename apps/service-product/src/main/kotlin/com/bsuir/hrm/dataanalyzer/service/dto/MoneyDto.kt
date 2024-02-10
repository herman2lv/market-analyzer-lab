package com.bsuir.hrm.dataanalyzer.service.dto

import java.math.BigDecimal

data class MoneyDto(
    var amount: BigDecimal,
    var currency: Currency,
) {
    enum class Currency {
        BYN, RUB, USD, EUR,
    }
}
