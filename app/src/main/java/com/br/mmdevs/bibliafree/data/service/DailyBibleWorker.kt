package com.br.mmdevs.bibliafree.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.br.mmdevs.bibliafree.R
import com.br.mmdevs.bibliafree.domain.usecase.BibliaManager
import com.br.mmdevs.bibliafree.presentation.livros_feature.getNomeLivro
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject
@HiltWorker
class DailyBibleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val manager: BibliaManager
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val livros = manager.getAllLivrosFromJson()
            val livroAleatorio = livros.random()
            val capituloAleatorio = livroAleatorio.chapters!!.random()
            val versiculoAleatorio = capituloAleatorio.random()
            val nomeLivro = getNomeLivro(livroAleatorio.abbrev)

            showNotification(nomeLivro, versiculoAleatorio)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(titulo: String, texto: String) {
        val channelId = "daily_verse_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val channel = NotificationChannel(
            channelId,
            "Versículo Diário",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_logo_biblia)
            .setContentTitle("Versículo em $titulo")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

            .build()

        notificationManager.notify(1, notification)
    }
}