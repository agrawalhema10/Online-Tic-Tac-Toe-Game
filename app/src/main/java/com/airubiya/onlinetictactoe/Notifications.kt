package com.airubiya.onlinetictactoe

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class Notifications(){
    val NOTIFYTAG="new request"
    fun Notify(context: Context,message:String,number:Int){
        val intent = Intent(context,MainActivity::class.java)
        val builder = NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("New Reqiuest")
            .setContentText(message)
            .setNumber(number)
            .setSmallIcon(R.drawable.tictac)

            .setContentIntent(PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT))
        val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFYTAG, 0, builder.build())
        }

        else{
            nm.notify(NOTIFYTAG,hashCode(),builder.build())
        }








    }
}