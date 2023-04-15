package com.amit.aquafill.di

import com.amit.aquafill.network.UserService
import com.amit.aquafill.repository.user.IUserRepository
import com.amit.aquafill.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl("https://aqua-fill.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
    }
    @Singleton
    @Provides
    fun provideUserService(retrofitBuilder: Retrofit.Builder): UserService {
        return retrofitBuilder.build().create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userService: UserService): IUserRepository {
        return UserRepository(userService)
    }
}