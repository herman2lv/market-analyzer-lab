package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.data.ReportRepository
import com.bsuir.hrm.dataanalyzer.data.entity.Report
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tenant/reports")
class ReportController(
    private val reportRepository: ReportRepository
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Report {
        return reportRepository.findById(id).orElseThrow()
    }

    @GetMapping
    fun getAll(): List<Report> {
        return reportRepository.findAll()
    }

    @PostMapping
    @ResponseStatus(CREATED)
    fun create(@RequestBody report: Report): Report {
        return reportRepository.save(report)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody report: Report): Report {
        report.id = id
        return reportRepository.save(report)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        reportRepository.deleteById(id)
    }
}
