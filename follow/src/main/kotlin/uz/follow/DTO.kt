package uz.follow

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class FollowRequest(
//    val followerId: Long,
    val followingId: Long
)

data class FollowResponse(
    val followerId: Long,
    val followingId: Long
){
    companion object {
        fun toResponse(follow: Follow) = FollowResponse(
            followerId = follow.followerId,
            followingId = follow.followingId
        )
    }
}

data class FollowDto(
    val id: Long,
    val username: String,
)

//data class UserDto(
//    val id: Long,
//    val username: String
//)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
)


