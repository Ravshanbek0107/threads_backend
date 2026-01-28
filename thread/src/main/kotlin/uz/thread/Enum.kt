package uz.thread

enum class ErrorCode(val code: Int) {
    THREAD_NOT_FOUND(201),

    FEIGN_ERROR(400),


}

enum class ThreadType {
    POST,
    REPLY,
    QUOTE
}

enum class ThreadStatus{
    ACTIVE,INACTIVE
}