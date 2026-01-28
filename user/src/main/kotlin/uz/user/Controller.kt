package uz.user

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): UserResponse {
        return userService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UserUpdateRequest): UserResponse {
        return userService.update(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): UserResponse {
        return userService.getOne(id)
    }

    @GetMapping
    fun getAll(): List<UserResponse> {
        return userService.getAll()
    }

    //follow

    @PostMapping("/follow")
    fun follow(@RequestBody request: FollowRequest): FollowResponse {
        return userService.follow(request)
    }

    @PostMapping("/unfollow")
    fun unfollow(@RequestBody request: FollowRequest) {
        userService.unfollow(request)
    }

    @GetMapping("/followers/{id}")
    fun followers(@PathVariable id: Long): List<UserResponse> {
        return userService.getFollowers(id)
    }

    @GetMapping("/followings/{id}")
    fun following(@PathVariable id: Long): List<UserResponse> {
        return userService.getFollowing(id)
    }

}