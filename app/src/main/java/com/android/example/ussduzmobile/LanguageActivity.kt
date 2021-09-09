package com.android.example.ussduzmobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.example.ussduzmobile.databinding.ActivityLanguageBinding
import java.util.*
import android.util.DisplayMetrics
import com.android.example.ussduzmobile.utils.*


class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var toEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val value = intent.extras?.getBoolean("to_edit")
        if (value != null) {
            toEdit = value
        }

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val isFirst = sharedPreferences.getBoolean("is_first", false)
        val lan = sharedPreferences.getString(LAN_KEY, "")


        if (isFirst && !toEdit) {
            toStart()
        }

        sharedPreferences.edit().putBoolean("is_first", true).apply()

        binding.apply {
            uz.setOnClickListener {
                toWrite(LAN_UZ_LATIN)
                toStart()
            }
            ru.setOnClickListener {
                toWrite(LAN_RU)
                toStart()
            }
//            en.setOnClickListener {
//                toWrite(LAN_UZ_CIRIL)
//                toStart()
//            }
        }

    }

    private fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    private fun toWrite(lan: String) {
        sharedPreferences.edit().putString(LAN_KEY, lan).apply()
    }

    private fun toStart() {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val config = context.resources.configuration
//        config.setLocale(locale)
//        context.createConfigurationContext(config)
//        context.resources.updateConfiguration(
//            config,
//            context.resources.displayMetrics
//        )
//        LocalHelper().setLocale(this, language)

        startActivity(Intent(this@LanguageActivity, SplashActivity::class.java))
        finish()
    }
}