package uz.follow

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface FollowService {
    fun follow(request: FollowRequest): FollowResponse
    fun unfollow(request: FollowRequest)
    fun getFollowers(page: Pageable): Page<FollowDto>
    fun getFollowing(page: Pageable): Page<FollowDto>
}

@Service
class UserServiceImpl(
    private val userClient: UserFeignClient,
    private val followRepository: FollowRepository
) : FollowService {

    @Transactional
    override fun follow(request: FollowRequest): FollowResponse {
        val followerId = getUserId()

        if (followerId == request.followingId) throw CannotFollowHimselfException()

        val following = userClient.getUser(request.followingId)

        val exists = followRepository.existsByFollowerIdAndFollowingId(followerId, following.id)

        if (exists) throw AlreadyFollowException()


        val follow = Follow(
            followerId = followerId,
            followerUsername = username(),
            followingId = following.id,
            followingUsername = following.username
        )

        followRepository.save(follow)

        return FollowResponse.toResponse(follow)
    }

    @Transactional
    override fun unfollow(request: FollowRequest) {

        val follower = getUserId()

        val following = userClient.getUser(request.followingId)

        val follows = followRepository.findByFollowerIdAndFollowingId(follower, following.id)
            ?: throw FollowNotFoundException()

        followRepository.delete(follows)

    }

    override fun getFollowers(page: Pageable): Page<FollowDto> {

        val user = getUserId()

        return followRepository.findAllByFollowingId(user,page).map {
            FollowDto(
                it.followerId,
                it.followerUsername,
            )
        }
    }

    override fun getFollowing(page: Pageable): Page<FollowDto> {

        val user = getUserId()

        return followRepository.findAllByFollowerId(user,page).map {
            FollowDto(
                it.followingId,
                it.followingUsername,
            )
        }
    }




}