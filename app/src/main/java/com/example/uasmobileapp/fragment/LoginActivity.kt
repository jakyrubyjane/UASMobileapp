package com.example.uasmobileapp.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uasmobileapp.R
import com.example.uasmobileapp.adapter.Adapter
import com.example.uasmobileapp.databinding.FragmentLoginActivityBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bindingLogin: FragmentLoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_activity)
        bindingLogin = FragmentLoginActivityBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)
        with(bindingLogin) {
            viewPagerLogin.adapter = Adapter(supportFragmentManager)
            // Hubungkan ViewPager dengan TabLayout
            tabLayoutLogin.setupWithViewPager(viewPagerLogin)
        }
    }
}
