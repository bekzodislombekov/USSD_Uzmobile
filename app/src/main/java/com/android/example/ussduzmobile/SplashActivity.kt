package com.android.example.ussduzmobile

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.android.example.ussduzmobile.databinding.ActivitySplashBinding
import com.android.example.ussduzmobile.utils.*
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private val TAG = "SplashActivity"
    private var isLoaded1 = false
    private var lan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        lan = sharedPreferences.getString(LAN_KEY, "")!!

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        if (!checkConnection()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (lan == LAN_RU) {
            viewModel.isLoaded2.observe(this, Observer {
                Log.d(TAG, "onCreate: $it")
                isLoaded1 = it
                if (isLoaded1) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            })
        } else {
            viewModel.isLoaded1.observe(this, Observer {
                Log.d(TAG, "onCreate: $it")
                isLoaded1 = it
                if (isLoaded1) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            })
        }


    }

    private fun checkConnection(): Boolean {
        var isConnected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            isConnected = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    isConnected = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return isConnected
    }
}


class SplashViewModel : ViewModel() {
    private var database: FirebaseDatabase
    private val reference1: DatabaseReference
    private val reference2: DatabaseReference

    private val _isLoaded1 = MutableLiveData<Boolean>().apply {
        database = FirebaseDatabase.getInstance(URL)
        reference1 = database.getReference(FIREBASE_LIST)

        reference1.child("$LAN_UZ_LATIN/$TARIFF_TEXT")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value = true
                }

                override fun onCancelled(error: DatabaseError) {
                    value = false
                }
            })
    }

    private val _isLoaded2 = MutableLiveData<Boolean>().apply {
        database = FirebaseDatabase.getInstance(URL)
        reference2 = database.getReference(FIREBASE_LIST)

        reference2.child("$LAN_RU/тариф").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value = true
            }

            override fun onCancelled(error: DatabaseError) {
                value = false
            }
        })
    }

    val isLoaded1: LiveData<Boolean> = _isLoaded1
    val isLoaded2: LiveData<Boolean> = _isLoaded2
}