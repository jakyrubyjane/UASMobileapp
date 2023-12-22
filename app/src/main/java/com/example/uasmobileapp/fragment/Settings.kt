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
import android.widget.Toast
import com.example.uasmobileapp.activity.AboutActivity
import com.example.uasmobileapp.activity.AccountActivity
import com.example.uasmobileapp.activity.MainActivity
import com.example.uasmobileapp.databinding.FragmentSettingsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = FirebaseFirestore.getInstance()
    private val moviesCollectionRef = db.collection("movies")
    private lateinit var executorService: ExecutorService
    private lateinit var bindingSettings: FragmentSettingsBinding

    private lateinit var sharedPreferenes : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        executorService = Executors.newSingleThreadExecutor()
        bindingSettings = FragmentSettingsBinding.inflate(layoutInflater)


        with(bindingSettings){
            ButtonLogout.setOnClickListener{
                val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("who", "guest")
                editor.putBoolean("isLoggedIn",false)
                editor.apply()


                Toast.makeText(requireContext(), "Logout Success", Toast.LENGTH_SHORT).show()
                val intentLogOut = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intentLogOut)
            }

            ButtonAboutUs.setOnClickListener{
                ButtonAboutUs.isClickable = true
                ButtonAboutUs.isEnabled = true
                val intentAccount = Intent(requireContext(), AboutActivity::class.java)
                startActivity(intentAccount)
            }

            ButtonAccount.setOnClickListener{
                ButtonAccount.isClickable = true
                ButtonAccount.isEnabled = true
                val intentAccount = Intent(requireContext(), AccountActivity::class.java)
                startActivity(intentAccount)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // nampilin tampilan fragment
        return bindingSettings.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}