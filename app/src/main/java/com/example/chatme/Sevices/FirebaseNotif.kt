
package com.example.chatme.Sevices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.media.app.NotificationCompat
import com.example.chatme.AppUtiles.AppUtiles
import com.example.chatme.Constants.AppConstants
import com.example.chatme.MessageActivity
import com.example.chatme.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import kotlin.collections.HashMap


class FirebaseNotif:FirebaseMessagingService() {

    private val appUtiles = AppUtiles()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        updatetoken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (p0.data.isNotEmpty()) {

            val map: Map<String, String> = p0.data

            val title = map["title"]
            val message = map["message"]
            val hisId = map["hisid"]
            val hisImage = map["hisimage"]
            val chatId = map["chatid"]

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                createoreonotifi(title!!, message!!, hisId!!, hisImage!!, chatId!!)
            else createNoramalNotification(title!!, message!!, hisId!!, hisImage!!, chatId!!)

        }
    }

    private fun updatetoken(token:String){
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(appUtiles.getUID()!!)

        val map:MutableMap<String,Any> = HashMap()
        map["token"] = token
        databaseReference.updateChildren(map)
    }
    private fun createNoramalNotification(title:String,message:String,hisid:String,hisimage:String,chatid:String){
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = androidx.core.app.NotificationCompat.Builder(this,AppConstants.CHENNAL_ID)
        builder.setContentTitle(title)
            .setContentText(message)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setColor(ResourcesCompat.getColor(resources,R.color.colorPrimary,null))
            .setSound(uri)
        val intent = Intent(this,MessageActivity::class.java)
        intent.putExtra("hisid",hisid)
        intent.putExtra("hisimage",hisimage)
        intent.putExtra("chatid",chatid)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val paddingintent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
            builder.setContentIntent(paddingintent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as  NotificationManager
        manager.notify(Random().nextInt(85-65),builder.build())

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createoreonotifi(title:String, message:String, hisid:String, hisimage:String, chatid:String){
        val channel = NotificationChannel(AppConstants.CHENNAL_ID,"Message",NotificationManager.IMPORTANCE_HIGH)
        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        val intent = Intent(this, MessageActivity::class.java)

        intent.putExtra("hisid", hisid)
        intent.putExtra("hisimage", hisimage)
        intent.putExtra("chatid", chatid)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = Notification.Builder(this, AppConstants.CHENNAL_ID )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            .setContentIntent(pendingIntent)
            .build()
        manager.notify(100, notification)
    }
}