package com.bsuir.hrm.dataanalyzer.service.dto

data class Dataset(
    val labels: List<String>,
    val dataSeriesList: MutableList<DataSeries> = mutableListOf()
) {

    fun addDataSeries(vararg dataSeries: DataSeries) {
        this.dataSeriesList.addAll(dataSeries)
    }
}
