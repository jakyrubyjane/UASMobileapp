package com.example.uasmobileapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.uasmobileapp.R
import com.example.uasmobileapp.activity.MainActivity
import com.example.uasmobileapp.databinding.FragmentRegisterBinding
import com.example.uasmobileapp.model.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : Fragment() {

    private lateinit var bindFragmentSignUp: FragmentRegisterBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var etUsername: EditText
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindFragmentSignUp = FragmentRegisterBinding.inflate(inflater, container, false)
        return bindFragmentSignUp.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFullName = bindFragmentSignUp.EditNameSignUp
        etUsername = bindFragmentSignUp.EditUsernameSignUp
        etEmail = bindFragmentSignUp.EditEmailSignUp
        etPassword = bindFragmentSignUp.EditPasswordSignUp

        with(bindFragmentSignUp) {
            ButtonSignUp.setOnClickListener {
                val username = etUsername.text.toString()
                val fullName = etFullName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (username.isNotEmpty() && fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            val account = Account(username, fullName, email, password)

                            db.collection("users")
                                .document(authResult.user!!.uid.toString())
                                .set(account)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign Up Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intentSignUp = Intent(requireContext(), MainActivity::class.java)
                                    etUsername.setText("")
                                    etFullName.setText("")
                                    etEmail.setText("")
                                    etPassword.setText("")
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign Up Failed, please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Sign Up Failed, please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            txtSignIn.setOnClickListener {
                val viewPager = requireActivity().findViewById<ViewPager>(R.id.viewPagerLogin)
                val signUpTabIndex = 0
                viewPager.currentItem = signUpTabIndex
            }
        }
    }
}