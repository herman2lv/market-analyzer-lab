package com.bsuir.hrm.dataanalyzer.scraper

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency,
)
