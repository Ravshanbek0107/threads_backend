package uz.user


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class UserCreateRequest(
    val username: String,
    val fullname: String,
    val bio: String,
    val password: String,
)

data class UserUpdateRequest(
    val username: String?,
    val fullname: String?,
    val bio: String?,
    val password: String?
)


data class UserResponse(
    val id: Long,
    val username: String,
    val fullname: String,
    val bio: String,
) {
    companion object {
        fun toResponse(user: User) = UserResponse(
            id = user.id!!,
            username = user.username,
            fullname = user.fullname,
            bio = user.bio,
        )
    }
}

data class FollowRequest(
    val followerId: Long,
    val followingId: Long
)

data class FollowResponse(
    val followerId: Long,
    val followingId: Long
){
    companion object {
        fun toResponse(follow:Follow) = FollowResponse(
            followerId = follow.follower.id!!,
            followingId = follow.following.id!!
        )
    }
}


