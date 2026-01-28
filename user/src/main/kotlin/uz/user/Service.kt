package uz.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

interface UserService {
    fun create(request: UserCreateRequest): UserResponse
    fun update(id: Long, request: UserUpdateRequest): UserResponse
    fun delete(id: Long)
    fun getOne(id: Long): UserResponse
    fun getAll(): List<UserResponse>

    //follow

    fun follow(request: FollowRequest): FollowResponse
    fun unfollow(request: FollowRequest)
    fun getFollowers(userId: Long): List<UserResponse>
    fun getFollowing(userId: Long): List<UserResponse>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) : UserService {

    @Transactional
    override fun create(request: UserCreateRequest): UserResponse {

        val user = userRepository.findAllNotDeleted().any{
            it.username == request.username
        }

        if (user) throw UserAlreadyExistsException()



        val newUser = User(
            username = request.username,
            fullname = request.fullname,
            bio = request.bio,
            password = request.password,
        )

        userRepository.save(newUser)
        return UserResponse.toResponse(newUser)
    }

    @Transactional
    override fun update(id: Long, request: UserUpdateRequest): UserResponse {

        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        request.username?.let { newUsername ->
            val exists = userRepository.findAllNotDeleted().any {
                it.username == newUsername && it.id != user.id
            }
            if (exists) throw UsernameAlreadyTakenException()

            user.username = newUsername
        }

        request.fullname?.let { user.fullname = it }
        request.bio?.let { user.bio = it }

        request.password?.let {
            user.password = it
        }

        userRepository.save(user)
        return UserResponse.toResponse(user)
    }

    @Transactional
    override fun delete(id: Long) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        userRepository.trash(user.id!!)
    }

    override fun getOne(id: Long): UserResponse {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        return UserResponse.toResponse(user)
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map{
            UserResponse.toResponse(it)
        }
    }

    @Transactional
    override fun follow(request: FollowRequest): FollowResponse {

        if (request.followerId == request.followingId) throw CannotFollowHimselfException()

        val follower = userRepository.findByIdAndDeletedFalse(request.followerId) ?: throw UserNotFoundException()

        val following = userRepository.findByIdAndDeletedFalse(request.followingId) ?: throw UserNotFoundException()

        val exists = followRepository.existsByFollowerAndFollowing(follower, following)

        if (exists) throw AlreadyFollowException()


        val follow = Follow(
            follower = follower,
            following = following
        )

        followRepository.save(follow)

        return FollowResponse.toResponse(follow)
    }

    @Transactional
    override fun unfollow(request: FollowRequest) {

        val follower = userRepository.findByIdAndDeletedFalse(request.followerId) ?: throw UserNotFoundException()

        val following = userRepository.findByIdAndDeletedFalse(request.followingId) ?: throw UserNotFoundException()

        val follows = followRepository.findByFollowerAndFollowing(follower, following) ?: throw FollowNotFoundException()

        followRepository.delete(follows)

    }

    override fun getFollowers(userId: Long): List<UserResponse> {

        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()

        return followRepository.findAllByFollowing(user).map {
                UserResponse.toResponse(it.follower)
            }
    }

    override fun getFollowing(userId: Long): List<UserResponse> {

        val user = userRepository.findByIdAndDeletedFalse(userId) ?: throw UserNotFoundException()

        return followRepository.findAllByFollower(user).map {
                UserResponse.toResponse(it.following)
            }
    }




}