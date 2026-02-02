package uz.zero.auth.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uz.zero.auth.model.responses.UserInfoResponse
import uz.zero.auth.services.UserService


@RestController
@RequestMapping("internal")
class AuthInternalController(
    private val userService: UserService
) {
    @GetMapping("/user-info")
    fun getUserInfo() = userService.userMe()

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Long): UserInfoResponse {
        return userService.getUserById(id)
    }

    @GetMapping("/test")
    fun testUserInfo() = "test info user"

}