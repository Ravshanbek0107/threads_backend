package uz.thread

import java.util.Date


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class ThreadCreateRequest(
    val authorId: Long,
    val type: ThreadType,
    val parentThreadId: Long? = null,
    val content: String? = null,
    val mediaIds: List<Long>? = emptyList()
)

data class ThreadResponse(
    val id: Long,
    val authorId: Long,
    val type: ThreadType,
    val parentThreadId: Long? = null,
    val content: String?,
    val createdDate: Date?,
    val media: List<MediaDto>?,
    val reactions: ReactionStatsResponse
)

data class MediaDto(
    val hashId: Long,
    val url: String,
    val type: String? = null,// file type
    val duration: Int? = null // video bolsa davom etishi
)

data class ReactionStatsResponse(
    val likeCount: Long,
    val viewCount: Long
)

data class UserDto(
    val id: Long,
    val username: String
)

data class MediaAttachRequest(
    val ownerId: Long,
    val hashIds: List<Long>
)


