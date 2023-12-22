package com.example.uasmobileapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uasmobileapp.R
import com.example.uasmobileapp.databinding.ActivityAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class AccountActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var bindingAcc: ActivityAccountBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        bindingAcc = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(bindingAcc.root)
        auth = Firebase.auth

        bindingAcc.TxtFullName.text =  auth.currentUser!!.email.toString()

    }
}