package uz.media

import org.springframework.web.multipart.MultipartFile


data class BaseMessage(val code: Int? = null, val message: String? = null) {
    companion object {
        var OK = BaseMessage(0, "OK")
    }
}

data class MediaUploadRequest(
    val files: List<MultipartFile>
)
data class MediaUploadResponse(
    val hashId: Long
)

data class MediaDto(
    val hashId: Long,
    val url: String
)

//internal
data class MediaAttachRequest(
    val ownerId: Long,
    val hashIds: List<Long>
)
