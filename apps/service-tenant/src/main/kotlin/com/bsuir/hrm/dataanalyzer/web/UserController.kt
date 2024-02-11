package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.data.UserRepository
import com.bsuir.hrm.dataanalyzer.data.entity.User
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
@RequestMapping("/tenant/users")
class UserController(
    private val userRepository: UserRepository
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): User {
        return userRepository.findById(id).orElseThrow()
    }

    @GetMapping
    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    @PostMapping
    @ResponseStatus(CREATED)
    fun create(@RequestBody user: User): User {
        return userRepository.save(user)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User): User {
        user.id = id
        return userRepository.save(user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userRepository.deleteById(id)
    }
}
