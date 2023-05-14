package com.bsuir.hrm.dataanalyzer.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class Money(
    var amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    var currency: Currency,
) {
    enum class Currency {
        BYN, RUB, USD, EUR,
    }
}
