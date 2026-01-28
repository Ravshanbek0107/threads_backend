package uz.user

enum class ErrorCode(val code: Int) {
    USER_NOT_FOUND(100),
    USER_ALREADY_EXISTS(101),
    USERNAME_ALREADY_TAKEN(102),
    USER_CANNOT_FOLLOW_HIMSELF(103),
    ALREADY_FOLLOW(104),
    FOLLOW_NOT_FOUND(105),

    FEIGN_ERROR(400),


}

enum class UserStatus{
    ACTIVE,INACTIVE
}