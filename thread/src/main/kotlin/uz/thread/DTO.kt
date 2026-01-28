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
    val content: String?,
    val createdDate: Date?,
    val media: List<MediaDto>?,
    val reactions: ReactionDto
)

data class MediaDto(
    val hashId: Long,
    val url: String,
    val type: String,
    val duration: Int? = null
)

data class ReactionDto(
    val likes: Long,
    val views: Long,
    val shares: Long
)

data class UserDto(
    val id: Long,
    val username: String
)

data class MediaAttachRequest(
    val ownerId: Long,
    val hashIds: List<Long>
)


