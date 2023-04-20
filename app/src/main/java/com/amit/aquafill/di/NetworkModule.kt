package com.amit.aquafill.di

import com.amit.aquafill.network.AuthInterceptor
import com.amit.aquafill.network.UserAuthService
import com.amit.aquafill.network.UserService
import com.amit.aquafill.repository.user.IUserRepository
import com.amit.aquafill.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder().baseUrl("https://aqua-fill.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideUserService(retrofitBuilder: Retrofit.Builder): UserService =
        retrofitBuilder.build().create(UserService::class.java)
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserAuth(retrofitBuilder: Builder, okHttpClient: OkHttpClient): UserAuthService =
        retrofitBuilder.client(okHttpClient).build().create(UserAuthService::class.java)

    @Singleton
    @Provides
    fun provideUserRepository(userService: UserService, userAuthService: UserAuthService):
            IUserRepository = UserRepository(userService, userAuthService)

}