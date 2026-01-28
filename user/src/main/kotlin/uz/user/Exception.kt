package uz.user

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

            is UserException-> {

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



sealed class UserException(message: String? = null) : RuntimeException(message) {
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

class UserNotFoundException : UserException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class UserAlreadyExistsException : UserException() {
    override fun errorType() = ErrorCode.USER_ALREADY_EXISTS
}

class UsernameAlreadyTakenException : UserException() {
    override fun errorType() = ErrorCode.USERNAME_ALREADY_TAKEN
}

class CannotFollowHimselfException : UserException() {
    override fun errorType() = ErrorCode.USER_CANNOT_FOLLOW_HIMSELF
}

class AlreadyFollowException : UserException() {
    override fun errorType() = ErrorCode.ALREADY_FOLLOW
}

class FollowNotFoundException : UserException() {
    override fun errorType() = ErrorCode.FOLLOW_NOT_FOUND
}


class FeignException(
    private val code: Int?,
    private val messageValue: String?
) :UserException() {

    override fun errorType() = ErrorCode.FEIGN_ERROR

    fun toBaseMessage() = BaseMessage(code, messageValue)

}


