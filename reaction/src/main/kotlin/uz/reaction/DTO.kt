package uz.reaction


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class ReactionStatsResponse(
    val likeCount: Long,
    val viewCount: Long
){
    companion object {
        fun toResponse(stat: ReactionStat) = ReactionStatsResponse(
            likeCount = stat.likeCount,
            viewCount = stat.viewCount
        )
    }
}

data class LikeRequest(
    val threadId: Long,
    val userId: Long
)

