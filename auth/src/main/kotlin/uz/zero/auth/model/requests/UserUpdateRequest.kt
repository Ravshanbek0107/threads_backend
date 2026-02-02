//package uz.davrbank.auth.models.requests
//
//import jakarta.validation.constraints.Size
//import uz.zero.auth.utils.NotSpace
//
//
//data class UserUpdateRequest(
//    @field:Size(min = 3, max = 32)
//    @field:NotSpace
//    val username: String,
//    val roleId: Long,
//    @field:Size(max = 255) val fullName: String
//)

package uz.zero.auth.model.requests

import jakarta.validation.constraints.Pattern
import uz.zero.auth.utils.NotSpace

data class UserUpdateRequest(
    val userId: Long,
    @field:NotSpace
    val username: String?,
    val fullName: String?,
    @field:Pattern.List(
        value = [
            Pattern(regexp = ".*[a-z].*", message = "PASSWORD_LOWER_CASE_ERROR"),
            Pattern(regexp = ".*[A-Z].*", message = "PASSWORD_UPPER_CASE_ERROR"),
            Pattern(regexp = ".*\\d.*", message = "PASSWORD_DIGIT_ERROR"),
            Pattern(
                regexp = ".*[~!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*",
                message = "PASSWORD_EXTRA_CHARACTER_ERROR"
            )
        ]
    )
    val password: String?
)



