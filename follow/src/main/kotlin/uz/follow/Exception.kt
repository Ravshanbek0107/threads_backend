package uz.follow

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.Locale

@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: ResourceBundleMessageSource,
) {
    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(exception: Throwable): ResponseEntity<Any> {
        when (exception) {
            is FeignException -> {
                return ResponseEntity
                    .badRequest()
                    .body(exception.toBaseMessage())
            }

            is FollowException-> {

                return ResponseEntity
                    .badRequest()
                    .body(exception.getErrorMessage(errorMessageSource))
            }

            else -> {
                exception.printStackTrace()
                return ResponseEntity
                    .badRequest().body(
                        BaseMessage(100,
                            "Iltimos support bilan bog'laning")
                    )
            }
        }
    }

}



sealed class FollowException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode
    protected open fun getErrorMessageArguments(): Array<Any?>? = null
    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                getErrorMessageArguments(),
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

class CannotFollowHimselfException : FollowException() {
    override fun errorType() = ErrorCode.USER_CANNOT_FOLLOW_HIMSELF
}

class AlreadyFollowException : FollowException() {
    override fun errorType() = ErrorCode.ALREADY_FOLLOW_TO_THIS_USER
}

class FollowNotFoundException : FollowException() {
    override fun errorType() = ErrorCode.FOLLOW_NOT_FOUND
}




class FeignException(
    private val code: Int?,
    private val messageValue: String?
) : FollowException() {

    override fun errorType() = ErrorCode.FEIGN_ERROR

    fun toBaseMessage() = BaseMessage(code, messageValue)

}


