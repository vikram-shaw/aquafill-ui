package com.amit.aquafill.repository.user


interface IUserRepository {
    suspend fun signup(email: String, password: String)
    suspend fun reset(email: String, password: String)
    suspend fun signing(email: String, password: String)
}