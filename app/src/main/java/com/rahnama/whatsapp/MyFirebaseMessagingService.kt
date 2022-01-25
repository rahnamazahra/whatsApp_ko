package com.rahnama.whatsapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){


    val channelId="notification_channel"
    val channelName="com.rahnama.whatsapp"

    //generate notification
    //attach the notification created with the custom layout
    //show notification


    override fun onMessageReceived(remotemessage: RemoteMessage) {
        if (remotemessage.notification !=null){
            generationNotif(remotemessage.notification!!.title!!,remotemessage.notification!!.body!!)
        }
    }
/**************************************************************************/

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, description: String):RemoteViews{
       val remoteViews=RemoteViews("com.rahnama.whatsapp",R.layout.notification)
        remoteViews.setTextViewText(R.id.titleNotif,title)
        remoteViews.setTextViewText(R.id.descriptionNotif,description)
        remoteViews.setImageViewResource(R.id.logoNotif,R.drawable.chat_icon)

        return remoteViews
    }

/**********************************************************************/
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generationNotif(title:String, description:String){

        val intent= Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)


        var notificationBuilder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.chat_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        notificationBuilder=notificationBuilder.setContent(getRemoteView(title,description))
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        notificationManager.notify(0,notificationBuilder.build())
    }

    /***********************************************************/
}