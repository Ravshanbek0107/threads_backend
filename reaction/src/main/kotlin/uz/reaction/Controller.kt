package uz.reaction

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/reaction")
class ReactionController(
    private val reactionService: ReactionService
) {


    @PostMapping("/like")
    fun toggleLike(@RequestBody request: LikeRequest) {
        reactionService.LikeOrUnlike(request.threadId,request.userId)
    }


    @PostMapping("/view/{threadId}")
    fun view(@PathVariable threadId: Long) {
        reactionService.view(threadId)
    }

    @GetMapping("/internal/thread/{threadId}")
    fun stats(@PathVariable threadId: Long): ReactionStatsResponse {
        return reactionService.stats(threadId)
    }

    @PostMapping("/internal/init/{threadId}")
    fun init(@PathVariable threadId: Long) {
        reactionService.init(threadId)
    }
}