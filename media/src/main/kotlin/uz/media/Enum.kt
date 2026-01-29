package uz.media

enum class ErrorCode(val code: Int) {
    UNSUPPORT_MEDIA_TYPE(310),
    MEDIA_SIZE_TOO_LARGE(311),
    MEDIA_NOT_FOUND(312),
    MEDIA_FILE_NOT_FOUND(313),

    FEIGN_ERROR(400),


}
