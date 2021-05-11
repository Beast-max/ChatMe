package com.example.chatme.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatme.AppUtiles.AppUtiles
import com.example.chatme.R
import com.example.chatme.databinding.ActivityEditNameBinding

class EditNameActivity : AppCompatActivity() {
    private lateinit var editNameBinding: ActivityEditNameBinding
    private lateinit var fName: String
    private lateinit var lName: String
    private lateinit var appUtiles: AppUtiles

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        editNameBinding = ActivityEditNameBinding.inflate(layoutInflater)
        setContentView(editNameBinding.root)
        appUtiles = AppUtiles()


        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name")
            if (name!!.contains(" ")) {
                val split = name.split(" ")
                editNameBinding.edtFName.setText(split[0])
                editNameBinding.edtLName.setText(split[1])

            }
        }

        editNameBinding.btnEditName.setOnClickListener {
            if (areFieldEmpty()) {
                val intent = Intent()

                intent.putExtra("name", "$fName $lName")
                setResult(100, intent)
                finish()
            }
        }


    }

    private fun areFieldEmpty(): Boolean {
        fName = editNameBinding.edtFName.text.toString()
        lName = editNameBinding.edtLName.text.toString()
        var required: Boolean = false
        var view: View? = null

        if (fName.isEmpty()) {
            editNameBinding.edtFName.error = "Field is required"
            required = true
            view = editNameBinding.edtFName

        } else if (lName.isEmpty()) {
            editNameBinding.edtLName.error = "Field is required"
            required = true
            view = editNameBinding.edtLName

        }

        return if (required) {
            view?.requestFocus()
            false
        } else true
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