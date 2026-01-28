package uz.media

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

            is MediaException-> {

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



sealed class MediaException(message: String? = null) : RuntimeException(message) {
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

class UnsupportMediaTypeException : MediaException() {
    override fun errorType() = ErrorCode.UNSUPPORT_MEDIA_TYPE
}

class MediaSizeTooLargeException : MediaException() {
    override fun errorType() = ErrorCode.MEDIA_SIZE_TOO_LARGE
}





class FeignException(
    private val code: Int?,
    private val messageValue: String?
) :MediaException() {

    override fun errorType() = ErrorCode.FEIGN_ERROR

    fun toBaseMessage() = BaseMessage(code, messageValue)

}


