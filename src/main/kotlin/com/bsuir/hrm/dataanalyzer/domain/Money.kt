package com.bsuir.hrm.dataanalyzer.domain

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency,
)
