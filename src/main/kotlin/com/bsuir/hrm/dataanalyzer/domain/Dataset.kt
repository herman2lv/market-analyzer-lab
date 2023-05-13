package com.bsuir.hrm.dataanalyzer.domain

data class Dataset(
    val labels: List<String>,
    val dataSeries: List<DataSeries>
)
