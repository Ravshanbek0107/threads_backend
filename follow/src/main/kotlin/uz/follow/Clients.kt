package uz.follow

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "auth", url = "\${services.hosts.auth}",configuration = [FeignOAuth2TokenConfig::class])
interface UserFeignClient {

    @GetMapping("/internal/users/{id}")
    fun getUser(@PathVariable id: Long): UserInfoResponse
}




