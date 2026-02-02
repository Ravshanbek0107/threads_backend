package uz.zero.auth.model.responses



import uz.zero.auth.entities.User
import uz.zero.auth.enums.Role
import uz.zero.auth.enums.UserStatus

data class UserResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: Role,
    val status: UserStatus
){
    companion object {
        fun toResponse(user: User) = UserResponse(
            id = user.id!!,
            fullName = user.fullName,
            username = user.username,
            role = user.role,
            status = user.status
        )
    }
}