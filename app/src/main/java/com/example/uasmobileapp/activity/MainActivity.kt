package com.example.uasmobileapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.uasmobileapp.Home
import com.example.uasmobileapp.R
import com.example.uasmobileapp.databinding.ActivityMainBinding
import com.example.uasmobileapp.fragment.Settings

class MainActivity : AppCompatActivity() {

    private lateinit var bindingPublicFragment: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindingPublicFragment = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingPublicFragment.root)
        loadFragment(Home())

        with(bindingPublicFragment){
            navigation.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.homeFragment-> {
                        loadFragment(Home())
                        true
                    }
                    R.id.settingFragment -> {
                        loadFragment(Settings())
                        true
                    }
                    else ->{
                        false}
                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}