package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Embeddable
data class Money(
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val currency: Currency,
) {
    enum class Currency {
        BYN, RUB, USD, EUR,
    }
}
