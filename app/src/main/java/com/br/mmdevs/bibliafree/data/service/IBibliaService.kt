package com.br.mmdevs.bibliafree.data.service

import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import retrofit2.http.GET

interface IBibliaService {
    @GET("livros")
    suspend fun getAllLivros():List<LivroEntity>
}