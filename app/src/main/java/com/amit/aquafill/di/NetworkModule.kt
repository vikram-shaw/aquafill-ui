package com.amit.aquafill.di

import com.amit.aquafill.network.AuthInterceptor
import com.amit.aquafill.network.CustomerService
import com.amit.aquafill.network.EntryService
import com.amit.aquafill.network.UserAuthService
import com.amit.aquafill.network.UserService
import com.amit.aquafill.repository.customer.CustomerRepository
import com.amit.aquafill.repository.customer.ICustomerRepository
import com.amit.aquafill.repository.entry.EntryRepository
import com.amit.aquafill.repository.entry.IEntryRepository
import com.amit.aquafill.repository.user.IUserRepository
import com.amit.aquafill.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Builder =
        Builder().baseUrl("https://dark-toad-sunglasses.cyclic.app")
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideUserService(retrofitBuilder: Builder): UserService =
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

    @Singleton
    @Provides
    fun provideCustomerAuth(retrofitBuilder: Builder, okHttpClient: OkHttpClient): CustomerService =
        retrofitBuilder.client(okHttpClient).build().create(CustomerService::class.java)

    @Singleton
    @Provides
    fun provideCustomerRepository(customerService: CustomerService):
            ICustomerRepository = CustomerRepository(customerService)
    @Singleton
    @Provides
    fun provideEntryAuth(retrofitBuilder: Builder, okHttpClient: OkHttpClient): EntryService =
        retrofitBuilder.client(okHttpClient).build().create(EntryService::class.java)

    @Singleton
    @Provides
    fun provideEntryRepository(entryService: EntryService):
            IEntryRepository = EntryRepository(entryService)
}