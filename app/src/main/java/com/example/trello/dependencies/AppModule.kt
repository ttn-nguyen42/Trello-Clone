package com.example.trello.dependencies

import com.example.trello.application.DispatcherProviderImpl
import com.example.trello.application.interfaces.DispatcherProvider
import com.example.trello.network.AppApi
import com.example.trello.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppApi(): AppApi = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build().create(AppApi::class.java)

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = DispatcherProviderImpl()
}