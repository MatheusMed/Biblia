package com.br.mmdevs.bibliafree.data.repository

import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.data.service.IBibliaService
import com.br.mmdevs.bibliafree.domain.repository.ILivroRepository
import javax.inject.Inject

class LivroRepositoryImpl @Inject constructor(
    private val api: IBibliaService
): ILivroRepository {
    override suspend fun getAllLivros(): List<LivroEntity> {
       return api.getAllLivros()
    }

}