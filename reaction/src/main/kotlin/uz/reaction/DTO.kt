package uz.reaction


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class ReactionStatsResponse(
    val threadId: Long,
    val likeCount: Long,
    val viewCount: Long
)

data class LikeRequest(
    val userId: Long
)

