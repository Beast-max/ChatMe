package com.example.chatme.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.chatme.AppUtiles.AppUtiles
import com.example.chatme.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class SpleshActivity : AppCompatActivity() {
    private var firebaseAuth:FirebaseAuth?=null
    private lateinit var appUtiles:AppUtiles
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splesh)
        appUtiles = AppUtiles()
        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({

            if (firebaseAuth!!.currentUser == null) {


                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {

                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener {
                        if (it.isSuccessful) {
                            val token = it.result?.token
                            val databaseReference =
                                FirebaseDatabase.getInstance().getReference("Users")
                                    .child(appUtiles.getUID()!!)

                            val map: MutableMap<String, Any> = HashMap()
                            map["token"] = token!!
                            databaseReference.updateChildren(map)
                        }
                    })
                startActivity(Intent(this, DashBoard::class.java))
                finish()
            }

        }, 3000)

    }
}