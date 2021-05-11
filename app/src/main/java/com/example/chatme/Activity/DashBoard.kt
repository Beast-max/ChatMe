package com.example.chatme.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.chatme.AppUtiles.AppUtiles
import com.example.chatme.Fragment.fragment_Contact
import com.example.chatme.Fragment.fragment_Profile
import com.example.chatme.Fragment.fragment_chat
import com.example.chatme.Fragment.fragment_story
import com.example.chatme.R
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoard : AppCompatActivity() {
    private lateinit var appUtiles: AppUtiles
    private  var fragment: Fragment?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.dashboardContainer, fragment_chat()).commit()
            bottomChip.setItemSelected(R.id.btnChat)
        }
        appUtiles= AppUtiles()
        bottomChip.setOnItemSelectedListener { id ->
            when (id) {
                R.id.btnChat -> {
                    fragment = fragment_chat()


                }
                R.id.btnContact -> {
                    fragment =   fragment_Contact()

                }

                R.id.btnstory -> {
                    fragment =   fragment_story()

                }

                R.id.btnProfile -> fragment = fragment_Profile()
            }

            fragment!!.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboardContainer, fragment!!)
                    .commit()
            }
        }

    }

    override fun onPostResume() {
        super.onPostResume()
        appUtiles.updateonlinestatus("offline")
    }

    override fun onResume() {
        super.onResume()
        appUtiles.updateonlinestatus("online")
    }
    }