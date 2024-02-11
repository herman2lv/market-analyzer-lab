package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.data.TenantRepository
import com.bsuir.hrm.dataanalyzer.data.entity.Tenant
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
@RequestMapping("/tenant/tenants")
class TenantController(
    private val tenantRepository: TenantRepository
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Tenant {
        return tenantRepository.findById(id).orElseThrow()
    }

    @GetMapping
    fun getAll(): List<Tenant> {
        return tenantRepository.findAll()
    }

    @PostMapping
    @ResponseStatus(CREATED)
    fun create(@RequestBody tenant: Tenant): Tenant {
        return tenantRepository.save(tenant)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody tenant: Tenant): Tenant {
        tenant.id = id
        return tenantRepository.save(tenant)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        tenantRepository.deleteById(id)
    }
}
