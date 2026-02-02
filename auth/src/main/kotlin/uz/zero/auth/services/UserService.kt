package uz.zero.auth.services

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uz.zero.auth.enums.Role
import uz.zero.auth.exceptions.UserAlreadyExistException
import uz.zero.auth.exceptions.UserNotFoundException
import uz.zero.auth.mappers.UserEntityMapper
import uz.zero.auth.model.requests.UserCreateRequest
import uz.zero.auth.model.requests.UserUpdateRequest
import uz.zero.auth.model.responses.UserInfoResponse
import uz.zero.auth.model.responses.UserResponse
import uz.zero.auth.repositories.UserRepository
import uz.zero.auth.utils.getUserJwtPrincipal
import uz.zero.auth.utils.userId

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserEntityMapper,
    private val passwordEncoder: PasswordEncoder,
) {
    fun userMe(): UserInfoResponse {
        return userMapper
            .toUserInfo(userRepository.findByIdAndDeletedFalse(userId())!!)
    }

    fun getUserById(id: Long): UserInfoResponse {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        return userMapper.toUserInfo(user)
    }

    @Transactional
    fun registerUser(request: UserCreateRequest): UserResponse {
        if (userRepository.existsByUsername(request.username)) throw UserAlreadyExistException()

        val user = userMapper.toEntity(request,Role.USER)
        userRepository.save(user)
        return UserResponse.toResponse(user)
    }


    @Transactional
    fun update(request: UserUpdateRequest): UserResponse {

        val user = userRepository.findByIdAndDeletedFalse(request.userId) ?: throw UserNotFoundException()

        request.username?.let { newUsername ->
            val exists = userRepository.findAllNotDeleted().any {
                it.username == newUsername && it.id != user.id
            }
            if (exists) throw UserAlreadyExistException()

            user.username = newUsername
        }

        request.fullName?.let { user.fullName = it }

        request.password?.let {
            user.password = passwordEncoder.encode(it)
        }

        userRepository.save(user)
        return UserResponse.toResponse(user)
    }

    @Transactional
    fun delete() {
        val id = userId()
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        userRepository.trash(user.id!!)
    }

    fun getOne(id: Long): UserResponse {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()

        return UserResponse.toResponse(user)
    }

    fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted().map{
            UserResponse.toResponse(it)
        }
    }
}