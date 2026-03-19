package com.br.mmdevs.bibliafree.domain.repository

import com.br.mmdevs.bibliafree.data.entity.LivroEntity

interface ILivroRepository {
    suspend fun getAllLivros(): List<LivroEntity>
}