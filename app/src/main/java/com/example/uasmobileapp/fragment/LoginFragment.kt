package com.example.uasmobileapp.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.uasmobileapp.Home
import com.example.uasmobileapp.R
import com.example.uasmobileapp.activity.MainActivity
import com.example.uasmobileapp.activity.MainActivityAdmin
import com.example.uasmobileapp.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreferenes : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        sharedPreferenes = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferenes.edit()
        val isLoggedIn = sharedPreferenes.getBoolean("isLoggedIn",false)

        Log.d("new000","${isLoggedIn}")
        Log.d("new000","${isLoggedIn}")
        Log.d("new000","${isLoggedIn}")

        if (isLoggedIn){
            val userType = sharedPreferenes.getString("who","guest")
            navigateTo(userType)
        } else {
        binding.ButtonSignIn.setOnClickListener {
            val enteredEmail = binding.EditUsernameSignIn.text.toString()
            val enteredPassword = binding.EditPasswordSignIn.text.toString()

            auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(requireActivity()) { task ->
                    val currentUser = auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        val userType = if (currentUser.email == "adminjekik@gmail.com") {
                            "admin"
                        } else {
                            "user"
                        }
                        // Simpan userType ke SharedPreferences
                        editor.putBoolean("isLoggedIn",true)
                        editor.putString("who", userType)
                        editor.apply()

                        navigateTo(userType)

                    } else {
                        Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

    private fun navigateTo(userType: String?) {
        val intent = when (userType) {
            "admin" -> Intent(requireActivity(),MainActivityAdmin::class.java)
            "user" -> Intent(requireActivity(), MainActivity::class.java)
            else -> null
        }
        requireActivity().startActivity(intent)

    }





}