package uz.zero.auth.controllers

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.services.UserService

@RestController
@RequestMapping("user")
class UserController(
    private val userService: UserService
){
    @PostMapping("/register")
    fun registerUser(@RequestBody request: UserCreateRequest) = userService.registerUser(request)

    @GetMapping("/me")
    fun userMe() = userService.userMe()

    @DeleteMapping("/delete")
    fun delete() = userService.delete()

    @PutMapping("/update")
    fun update(@RequestBody request: UserUpdateRequest) = userService.update(request)

    @GetMapping("/get-all")
    fun getAll() = userService.getAll()

    @GetMapping("/{id}")
    fun getAll(@PathVariable id:Long) = userService.getOne(id)

    @GetMapping("/test")
    fun testAdd() = "test adding "
}