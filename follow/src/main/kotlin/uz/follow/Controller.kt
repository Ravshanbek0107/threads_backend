package uz.follow




import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class FollowController(
    private val followService: FollowService,
) {

    @PostMapping("/follow")
    fun follow(@RequestBody request: FollowRequest): FollowResponse {
        return followService.follow(request)
    }

    @PostMapping("/unfollow")
    fun unfollow(@RequestBody request: FollowRequest) {
        followService.unfollow(request)
    }

    @GetMapping("/followers")
    fun followers(pageable: Pageable): Page<FollowDto> {
        return followService.getFollowers(pageable)
    }

    @GetMapping("/followings")
    fun following(pageable: Pageable): Page<FollowDto> {
        return followService.getFollowing(pageable)
    }

}