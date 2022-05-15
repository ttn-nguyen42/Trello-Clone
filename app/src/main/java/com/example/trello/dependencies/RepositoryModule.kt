package com.example.trello.dependencies

import com.example.trello.network.AppApi
import com.example.trello.repository.AuthenticationRepositoryImpl
import com.example.trello.repository.NetworkRepositoryImpl
import com.example.trello.repository.interfaces.AuthenticationRepository
import com.example.trello.repository.interfaces.NetworkRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthenticationRepository(api: FirebaseAuth): AuthenticationRepository = AuthenticationRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideNetworkRepository(api: AppApi): NetworkRepository = NetworkRepositoryImpl(api)
}