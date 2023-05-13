package com.bsuir.hrm.dataanalyzer.domain

data class Dataset(
    val labels: List<String>,
    private val dataSeriesList: MutableList<DataSeries> = mutableListOf()
) {

    fun addDataSeries(vararg dataSeries: DataSeries) {
        this.dataSeriesList.addAll(dataSeries)
    }
}
