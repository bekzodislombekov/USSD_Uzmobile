package com.android.example.ussduzmobile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.android.example.ussduzmobile.databinding.ActivityMainBinding
import com.android.example.ussduzmobile.models.NetData
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.ui.NotificationFragment
import com.android.example.ussduzmobile.ui.home.HomeFragment
import com.android.example.ussduzmobile.ui.home.lists.ListFragment
import com.android.example.ussduzmobile.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : FragmentActivity() {

    companion object {
        const val HOME_TAB = "HOME_TAB"
        const val BALANCE_TAB = "BALANCE_TAB"
        const val OPERATOR_TAB = "OPERATOR_TAB"
        const val NOTIFICATION_TAB = "NOTIFICATION_TAB"
        const val SETTINGS_TAB = "SETTINGS_TAB"
    }

    private lateinit var binding: ActivityMainBinding
    private var currentTab = ""
    private val TAG = "MainActivity"
    private var language: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        language = sharedPreferences.getString(LAN_KEY, "")!!

        binding.apply {
            if (language == LAN_RU) {
                balanceTv.setText(R.string.balance_title_ru)
                operatorTv.setText(R.string.operator_title_ru)
                notificationTv.setText(R.string.notification_title_ru)
                settingsTv.setText(R.string.settings_title_ru)
            } else if (language == LAN_UZ_CIRIL) {
                balanceTv.setText(R.string.balance_title_uz)
                operatorTv.setText(R.string.operator_title_uz)
                notificationTv.setText(R.string.notification_title_uz)
                settingsTv.setText(R.string.settings_title_uz)
            }
        }

        setListener()
        changeTab(HOME_TAB)
    }

    private fun setListener() {
        binding.apply {
            homeBtn.setOnClickListener {
                changeTab(HOME_TAB)
            }
            linearBalance.setOnClickListener {
                changeTab(BALANCE_TAB)
            }
            linearOperator.setOnClickListener {
                changeTab(OPERATOR_TAB)
            }
            linearNotification.setOnClickListener {
                changeTab(NOTIFICATION_TAB)
            }
            linearSettings.setOnClickListener {
                changeTab(SETTINGS_TAB)
            }
        }
    }

    private fun changeTab(selectedTab: String) {
        if (supportFragmentManager == null) {
            return
        }

        if (currentTab == selectedTab) {
            val i = supportFragmentManager.backStackEntryCount
            if (i == 1) {
                val backStackEntryAt = supportFragmentManager.getBackStackEntryAt(i - 1)
                supportFragmentManager.popBackStack(
                    backStackEntryAt.id,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
            return
        }
//        resetNavigationBarButton()

        var currentFragment: Fragment?

        when (selectedTab) {
            HOME_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()

                when (currentFragment) {
                    null -> {
                        currentFragment = HomeFragment.newInstance("index 0", language)
                        addFragment(currentFragment, HOME_TAB)
                    }
                    else -> {
                        currentFragment.childFragmentManager
                        showFragment(currentFragment)
                    }
                }
            }

            BALANCE_TAB -> {
//                currentTab = selectedTab
//                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)
//
//                resetTab()
//
//                if (currentFragment == null) {
//                    currentFragment = BalanceFragment.newInstance("index 1")
//                    addFragment(currentFragment, BALANCE_TAB)
//                } else {
//                    showFragment(currentFragment)
//                }
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.fromParts("tel", "*100#", null)
                startActivity(intent)
            }
            OPERATOR_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()

                if (currentFragment == null) {
                    val i = supportFragmentManager.backStackEntryCount
                    if (i == 1) {
                        val backStackEntryAt = supportFragmentManager.getBackStackEntryAt(i - 1)
                        supportFragmentManager.popBackStack(
                            backStackEntryAt.id,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }

                    currentFragment = when (language) {
                        LAN_RU -> {
                            ListFragment.newInstance("????????????????")
                        }
                        LAN_UZ_CIRIL -> {
                            ListFragment.newInstance("????????????????")
                        }
                        else -> {
                            ListFragment.newInstance("operator")
                        }
                    }
                    addFragment(currentFragment, OPERATOR_TAB)
                } else {
                    showFragment(currentFragment)
                }
            }
            NOTIFICATION_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()
                if (language == LAN_RU) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://uztelecom.uz/ru/novosti/novosti")
                    )
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://uztelecom.uz/uz/yangiliklar/yangiliklar")
                    )
                    startActivity(intent)
                }


//                if (currentFragment == null) {
//                    currentFragment = NotificationFragment.newInstance("index 3")
//                    addFragment(currentFragment, NOTIFICATION_TAB)
//                } else {
//                    showFragment(currentFragment)
//                }
            }
            SETTINGS_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()

                val intent = Intent(this, LanguageActivity::class.java)
                intent.putExtra("to_edit", true)
                startActivity(
                    intent
                )
                finish()
//                if (currentFragment == null) {
//                    currentFragment = SettingsFragment.newInstance("index 4")
//                    addFragment(currentFragment, SETTINGS_TAB)
//                } else {
//                    showFragment(currentFragment)
//                }
            }
        }

    }

    private fun resetTab() {
        var fragment = supportFragmentManager.findFragmentByTag(HOME_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(BALANCE_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(OPERATOR_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(NOTIFICATION_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(SETTINGS_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

    }

    private fun resetNavigationBarButton() {
        binding.apply {
            linearBalance.setBackgroundColor(getColor(R.color.uzmobile3))
            linearOperator.setBackgroundColor(getColor(R.color.uzmobile3))
            linearNotification.setBackgroundColor(getColor(R.color.uzmobile3))
            linearSettings.setBackgroundColor(getColor(R.color.uzmobile3))
        }
    }

    private fun updateNavigationBarButton() {

        when (currentTab) {
            BALANCE_TAB -> {
                binding.linearBalance.setBackgroundColor(getColor(R.color.white))
            }
            OPERATOR_TAB -> {
                binding.linearOperator.setBackgroundColor(getColor(R.color.white))
            }
            NOTIFICATION_TAB -> {
                binding.linearNotification.setBackgroundColor(getColor(R.color.white))
            }
            SETTINGS_TAB -> {
                binding.linearSettings.setBackgroundColor(getColor(R.color.white))
            }
        }
    }


    private fun addFragment(fragment: Fragment, tag: String) {
        if (fragment.isAdded) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(TRANSIT_FRAGMENT_FADE)
        transaction.add(R.id.frameLayoutFragment, fragment, tag).commitAllowingStateLoss()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
    }


    private fun writeRussianTariffToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_LIST/$LAN_RU/??????????")
        val tariff1 = TariffData(
            "???????????? 10",
            "",
            "",
            "10 ??????????",
            "10 MB",
            "10 SMS",
            "10 000 ??????????",
            "*111*1*11*12#"
        )
        myRef.child("???????????? 10").setValue(tariff1)
        val tariff2 = TariffData(
            "Street",
            "",
            "",
            "1500 ??????????",
            "6500 MB",
            "750 SMS",
            "39 900 ??????????",
            "*111*1*11*1*1#"
        )
        myRef.child("Street").setValue(tariff2)
        val tariff3 = TariffData(
            "Onlime",
            "",
            "",
            "2000 ??????????",
            "10000 MB",
            "1000 SMS",
            "49 900 ??????????",
            "*111*1*11*6#"
        )
        myRef.child("Onlime").setValue(tariff3)

        val tariff4 = TariffData(
            "Flash UPGRADE",
            "",
            "",
            "8000 ??????????",
            "64000 MB",
            "6000 SMS",
            "209 700 ??????????",
            "*111*1*11*5*1#"
        )
        myRef.child("Flash UPGRADE").setValue(tariff4)
    }

    private fun writeUzbekOperatorToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_LIST/$LAN_UZ_LATIN/operator")
        val op1 = NetData("", "", "", "", "", "INTERNET SAHIFASI\nUZMOBILE.UZ", "", "")
        myRef.child("INTERNET SAHIFASI").setValue(op1)
        val op2 = NetData("", "", "", "", "", "ISHONCH RAQAMI\n1099", "", "")
        myRef.child("ISHONCH RAQAMI").setValue(op2)
        val op3 = NetData("", "", "", "", "", "MUROJAAT YUBORISH\nINFO@UZTELECOM.UZ", "", "")
        myRef.child("MUROJAAT YUBORISH").setValue(op3)
    }

    private fun writeUSSDToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_LIST/$LAN_RU/ussd")
        val op1 = NetData("", "", "", "", "", "????????????", "", "*102#")
        myRef.child("????????????").setValue(op1)
        val op2 = NetData("", "", "", "", "", "?????? ??????????", "", "*104#")
        myRef.child("?????? ??????????").setValue(op2)
        val op3 = NetData("", "", "", "", "", "???????????????????? ??????????, ???????????????? ?? SMS", "", "*100*2#")
        myRef.child("???????????????????? ??????????, ???????????????? ?? SMS").setValue(op3)
    }


    private fun writeRussianMessageToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/??????")
        val mess1 = NetData(
            "420 ??????????",
            "1 ????????",
            "",
            "",
            "50",
            "50 SMS",
            "50",
            "*147*10043*28585#"
        )
        myRef.child("???????????????????? SMS ????????????/50 SMS").setValue(mess1)

        val mess2 = NetData(
            "840 ??????????",
            "1 ????????",
            "",
            "",
            "10",
            "10 SMS",
            "100",
            "*147*10043*28585#"
        )
        myRef.child("???????????????????? SMS ????????????//10 SMS").setValue(mess2)

        for (i in 1..5) {
            val netData = NetData(
                "${i}000 ??????????",
                "30 ????????",
                "",
                "",
                "${i}00",
                "${i}00 SMS",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("?????????????????????? SMS ????????????/${i}00 SMS").setValue(netData)
        }

        for (i in 1..5) {
            val netData = NetData(
                "${i + 4}000 ??????????",
                "30 ????????",
                "",
                "",
                "${i}0",
                "${i}0 SMS",
                "${i}0",
                "*147*10043*28585#"
            )
            myRef.child("?????????????????????????? SMS ????????????/${i}0 SMS").setValue(netData)
        }

    }

    //
    private fun writeRussianMinuteToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/??????????")

        for (i in 1..7 step 2) {
            val netData = NetData(
                "${i + 3}000 ??????????",
                "30 ????????",
                "",
                "${i}00",
                "",
                "${i}00 ??????????",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("???????????? ??????????/${i}00 ??????????").setValue(netData)
        }

        for (i in 1..6) {
            val netData = NetData(
                "${i + 2}000 ??????????",
                "30 ????????",
                "",
                "${i}00",
                "",
                "${i}00 ??????????",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("A???????????????? Constructor/${i}00 ??????????").setValue(netData)
        }

    }

    //
    private fun writeRussianServiceToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/????????????")
        val service1 = NetData(
            "",
            "",
            "",
            "",
            "",
            "LTE (4G)",
            "LTE",
            "*147*10043*28585#"
        )
        myRef.child("????????????/LTE (4G)").setValue(service1)
        val service2 = NetData(
            "",
            "",
            "",
            "",
            "",
            "???????????? 'Restart'",
            "",
            "*147*10043*28585#"
        )
        myRef.child("????????????/???????????? 'Restart'").setValue(service2)

        val service3 = NetData(
            "",
            "",
            "",
            "",
            "",
            "?????????????????? ??????????",
            "",
            "*147*10043*28585#"
        )
        myRef.child("????????????/?????????????????? ??????????").setValue(service3)

        val service4 = NetData(
            "",
            "",
            "",
            "",
            "",
            "???? ????????",
            "",
            "*147*10043*28585#"
        )
        myRef.child("??????????????/???? ????????").setValue(service4)

        val service5 = NetData(
            "",
            "",
            "",
            "",
            "",
            "????????????",
            "",
            "*147*10043*28585#"
        )
        myRef.child("??????????????/????????????").setValue(service5)

    }

    //
    private fun writeRussianInternetToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/????????????????")

        for (i in 1..5) {
            val netData = NetData(
                "${i}000 ??????????",
                "1 ????????",
                "${i}00 MB",
                "",
                "",
                "?????????????? ${i}00 MB",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("???????????????????? ????????????/?????????????? ${i}00 MB").setValue(netData)
        }

        for (i in 1..5) {
            val netData = NetData(
                "${i}0000 ??????????",
                "30 ????????",
                "${i}0 GB",
                "",
                "",
                "${i}0 GB",
                "${i}0 GB",
                "*147*10043*28585#"
            )
            myRef.child("?????????????????????? ????????????/${i}0 GB").setValue(netData)
        }

        for (i in 5..15 step 3) {
            val netData = NetData(
                "${i}0000 ??????????",
                "30 ????????",
                "${i}0 GB",
                "",
                "",
                "${i}0 GB",
                "${i}0",
                "*147*10043*28585#"
            )
            myRef.child("???????????????? non-stop/${i}0 GB").setValue(netData)
        }

        val netData1 = NetData(
            "6300 ??????????",
            "1 ????????",
            "12 000 MB",
            "",
            "",
            "????????",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("???????????? ????????????????/??????").setValue(netData1)

        val netData2 = NetData(
            "31 500 ??????????",
            "7 ??????????",
            "12 000 MB",
            "",
            "",
            "7 ??????????",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("???????????? ????????????????/7 ??????????").setValue(netData2)

        val netData3 = NetData(
            "99 000 ??????????",
            "30 ??????????",
            "12 000 MB",
            "",
            "",
            "30 ??????????",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("???????????? ????????????????/30 ??????????").setValue(netData3)
    }
}