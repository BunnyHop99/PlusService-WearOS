package com.example.anew

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat.WearableExtender
import java.util.*

class MainActivity : Activity(){

    var boton : Button ?= null

    private val channelID = "1"
    private val notificationId = 0
    private val BUTTON_INTENT_REQUEST = 1

    companion object {
        const val INTENT_REQUEST = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        var intent = Intent(this,ImportantActivity::class.java)
        var pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(INTENT_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val buttonIntent = Intent(this, ImportantActivity::class.java)
        buttonIntent.putExtra("EXTRA_ARG", "Boton presionado")
        val buttonPendingIntent = PendingIntent.getActivity(
            this,
            BUTTON_INTENT_REQUEST,
            buttonIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_stat_name,
            "Boton",
            buttonPendingIntent
        ).build()

        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .addAction(action)
            .setContentTitle("Andres")
            .setContentText("Buenas tardes")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)

        boton = findViewById(R.id.btn_click)

        boton!!.setOnClickListener{
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }

    }

    private fun createNotificationChannel() {
        var CHANNEL_ID = "1"
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
