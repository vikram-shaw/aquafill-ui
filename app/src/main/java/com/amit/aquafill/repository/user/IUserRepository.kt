package com.amit.aquafill.repository.user

import com.amit.aquafill.domain.model.User

interface IUserRepository {
    suspend fun signup(email: String, password: String): Pair<User, String>
    suspend fun reset(email: String, password: String): Pair<User, String>
    suspend fun signin(email: String, password: String): Pair<User, String>
}