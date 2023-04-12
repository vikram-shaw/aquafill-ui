package com.amit.aquafill.repository.user

import com.amit.aquafill.domain.model.User

class UserRepository: IUserRepository {
    override suspend fun signup(email: String, password: String): Pair<User, String> {
        TODO("Not yet implemented")
    }

    override suspend fun reset(email: String, password: String): Pair<User, String> {
        TODO("Not yet implemented")
    }

    override suspend fun signin(email: String, password: String): Pair<User, String> {
        TODO("Not yet implemented")
    }

}