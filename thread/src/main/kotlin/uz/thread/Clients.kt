package uz.thread

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "user", url = "\${services.hosts.user}")
interface UserFeignClient {

    @GetMapping("/api/v1/user/{id}")
    fun getUser(@PathVariable id: Long): UserDto
}


@FeignClient(name = "media", url = "\${services.hosts.media}")
interface MediaFeignClient {

    @PostMapping("/api/v1/media/internal/set-thread")
    fun setThreadId(@RequestBody request: MediaAttachRequest)

    @PostMapping("/api/v1/media/internal/get-images")
    fun getImagesByHashId(@RequestBody ids: List<Long>): List<MediaDto>
}

@FeignClient(name = "reaction", url = "\${services.hosts.reaction}")
interface ReactionFeignClient {

    @PostMapping("/api/v1/reaction/internal/init/{threadId}")
    fun init(@PathVariable threadId: Long)

    @GetMapping("/api/v1/reaction/internal/thread/{threadId}")
    fun stats(@PathVariable threadId: Long): ReactionStatsResponse
}


