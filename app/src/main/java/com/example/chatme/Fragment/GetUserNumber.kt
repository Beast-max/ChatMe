package com.example.chatme.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chatme.R
import com.example.chatme.UserModel
import com.fevziomurtekin.customprogress.Dialog
import com.fevziomurtekin.customprogress.Type
import com.fevziomurtekin.customprogress.Visibility
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_get_user_number.view.*
import java.util.concurrent.TimeUnit

class GetUserNumber : Fragment() {
    private lateinit var progressbar : Dialog
    private var number: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var code: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get_user_number, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        view.btnGenerateOTP.setOnClickListener {
            if (checkNumber()) {
                val phoneNumber = view.countryCodePicker.selectedCountryCodeWithPlus + number
                sendCode(phoneNumber)
                progressbar =getView()!!.findViewById( R.id.progress)
                progressbar.settype(Type.PACMAN)

                progressbar.show()

            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userModel =
                            UserModel(
                                "", "", "",
                                firebaseAuth!!.currentUser!!.phoneNumber!!,
                                firebaseAuth!!.uid!!
                            )

                        databaseReference!!.child(firebaseAuth!!.uid!!).setValue(userModel)
                        activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.main_container,getUserData())
                            ?.commit()
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException)
                    Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
                else if (e is FirebaseTooManyRequestsException)
                    Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
                else Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationCode: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                code = verificationCode
                activity!!.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, VerifyNumber.newInstance(code!!))
                    .commit()
                progressbar.gone()

            }
        }

        return view
    }

    private fun sendCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            activity!!,
            callbacks
        )

    }

    private fun checkNumber(): Boolean {
        number = view!!.edtNumber.text.toString().trim()
        if (number!!.isEmpty()) {
            view!!.edtNumber.error = "Field is required"
            return false
        } else if (number!!.length < 10) {
            view!!.edtNumber.error = "Number should be 10 in length"
            return false
        } else return true
    }


}