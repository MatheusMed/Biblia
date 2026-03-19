package com.br.mmdevs.bibliafree.di

import android.content.Context
import com.br.mmdevs.bibliafree.data.datastore.SettingsRepository
import com.br.mmdevs.bibliafree.data.service.AuthInterceptor
import com.br.mmdevs.bibliafree.data.service.IBibliaService
import com.br.mmdevs.bibliafree.data.service.JsonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://biblia.marciocosta.eti.br/v1/"
    private const val TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlN1biBOb3YgMjAgMjAyMiAyMzowMjowNSBHTVQrMDAwMC42MzU4Njk0ZGZkYmFkYzAwMjQzMmUxMTMiLCJpYXQiOjE2Njg5ODUzMjV9.AgDfJDubMjW00ywmOnMrhZD7CbzsRfoR8cTCHNjItL4"
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor(TOKEN))
//            .build()
//    }

    @Provides
    fun provideJsonDataSource(
        @ApplicationContext context: Context
    ): JsonDataSource {
        return JsonDataSource(context)
    }
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepository(context)
    }

    @Provides
    @Singleton
    fun provideBibliaService(retrofit: Retrofit): IBibliaService {
        return retrofit.create(IBibliaService::class.java)
    }
}