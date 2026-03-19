package com.br.mmdevs.bibliafree.di

import com.br.mmdevs.bibliafree.data.repository.LivroRepositoryImpl
import com.br.mmdevs.bibliafree.domain.repository.ILivroRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindLivroRepository(
        impl: LivroRepositoryImpl
    ): ILivroRepository
}