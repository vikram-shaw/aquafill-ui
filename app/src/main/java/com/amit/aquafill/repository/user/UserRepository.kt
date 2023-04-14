package com.amit.aquafill.repository.user

import com.amit.aquafill.domain.model.User
import com.amit.aquafill.network.UserService
import com.amit.aquafill.network.model.UserDto
import com.amit.aquafill.network.model.UserDtoMapper

class UserRepository(
    private val userService: UserService,
    private val mapper: UserDtoMapper
): IUserRepository {
    override suspend fun signup(email: String, password: String): Pair<User, String> {
        val response = userService.signup(user = User(email, password))
        return Pair(mapper.mapToDomainModel(response.user), response.token)
    }

    override suspend fun reset(email: String, password: String): Pair<User, String> {
        val response = userService.reset(user = User(email, password))
        return Pair(mapper.mapToDomainModel(response.user), response.token)
    }

    override suspend fun signin(email: String, password: String): Pair<User, String> {
        val response = userService.signing(user = User(email, password))
        return Pair(mapper.mapToDomainModel(response.user), response.token)
    }

}