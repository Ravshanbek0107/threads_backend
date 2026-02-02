package uz.thread

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody



@FeignClient(name = "media", url = "\${services.hosts.media}",configuration = [FeignOAuth2TokenConfig::class])
interface MediaFeignClient {

    @PostMapping("internal/set-thread")
    fun setThreadId(@RequestBody request: MediaAttachRequest)

    @PostMapping("internal/get-images")
    fun getImagesByHashId(@RequestBody ids: List<Long>): List<MediaDto>
}

@FeignClient(name = "reaction", url = "\${services.hosts.reaction}",configuration = [FeignOAuth2TokenConfig::class])
interface ReactionFeignClient {

    @PostMapping("internal/init/{threadId}")
    fun init(@PathVariable threadId: Long)

    @GetMapping("internal/thread/{threadId}")
    fun stats(@PathVariable threadId: Long): ReactionStatsResponse
}


