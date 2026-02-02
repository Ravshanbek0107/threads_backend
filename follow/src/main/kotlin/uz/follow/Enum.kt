package uz.follow

enum class ErrorCode(val code: Int) {
    USER_CANNOT_FOLLOW_HIMSELF(150),
    ALREADY_FOLLOW_TO_THIS_USER(151),
    FOLLOW_NOT_FOUND(152),

    FEIGN_ERROR(400),


}

