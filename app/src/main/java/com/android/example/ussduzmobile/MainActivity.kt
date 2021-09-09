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
                            ListFragment.newInstance("оператор")
                        }
                        LAN_UZ_CIRIL -> {
                            ListFragment.newInstance("оператор")
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
            database.getReference("$FIREBASE_LIST/$LAN_RU/тариф")
        val tariff1 = TariffData(
            "Просто 10",
            "",
            "",
            "10 минут",
            "10 MB",
            "10 SMS",
            "10 000 сумов",
            "*111*1*11*12#"
        )
        myRef.child("Просто 10").setValue(tariff1)
        val tariff2 = TariffData(
            "Street",
            "",
            "",
            "1500 минут",
            "6500 MB",
            "750 SMS",
            "39 900 сумов",
            "*111*1*11*1*1#"
        )
        myRef.child("Street").setValue(tariff2)
        val tariff3 = TariffData(
            "Onlime",
            "",
            "",
            "2000 минут",
            "10000 MB",
            "1000 SMS",
            "49 900 сумов",
            "*111*1*11*6#"
        )
        myRef.child("Onlime").setValue(tariff3)

        val tariff4 = TariffData(
            "Flash UPGRADE",
            "",
            "",
            "8000 минут",
            "64000 MB",
            "6000 SMS",
            "209 700 сумов",
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
        val op1 = NetData("", "", "", "", "", "Баланс", "", "*102#")
        myRef.child("Баланс").setValue(op1)
        val op2 = NetData("", "", "", "", "", "Мой номер", "", "*104#")
        myRef.child("Мой номер").setValue(op2)
        val op3 = NetData("", "", "", "", "", "Оставшееся время, Интернет и SMS", "", "*100*2#")
        myRef.child("Оставшееся время, Интернет и SMS").setValue(op3)
    }


    private fun writeRussianMessageToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/смс")
        val mess1 = NetData(
            "420 сумов",
            "1 день",
            "",
            "",
            "50",
            "50 SMS",
            "50",
            "*147*10043*28585#"
        )
        myRef.child("Ежедневные SMS пакеты/50 SMS").setValue(mess1)

        val mess2 = NetData(
            "840 сумов",
            "1 день",
            "",
            "",
            "10",
            "10 SMS",
            "100",
            "*147*10043*28585#"
        )
        myRef.child("Ежедневные SMS пакеты//10 SMS").setValue(mess2)

        for (i in 1..5) {
            val netData = NetData(
                "${i}000 сумов",
                "30 дней",
                "",
                "",
                "${i}00",
                "${i}00 SMS",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("Ежемесячные SMS пакеты/${i}00 SMS").setValue(netData)
        }

        for (i in 1..5) {
            val netData = NetData(
                "${i + 4}000 сумов",
                "30 дней",
                "",
                "",
                "${i}0",
                "${i}0 SMS",
                "${i}0",
                "*147*10043*28585#"
            )
            myRef.child("Международные SMS пакеты/${i}0 SMS").setValue(netData)
        }

    }

    //
    private fun writeRussianMinuteToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/минут")

        for (i in 1..7 step 2) {
            val netData = NetData(
                "${i + 3}000 сумов",
                "30 дней",
                "",
                "${i}00",
                "",
                "${i}00 МИНУТ",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("Пакеты минут/${i}00 МИНУТ").setValue(netData)
        }

        for (i in 1..6) {
            val netData = NetData(
                "${i + 2}000 сумов",
                "30 дней",
                "",
                "${i}00",
                "",
                "${i}00 МИНУТ",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("Aбонентов Constructor/${i}00 МИНУТ").setValue(netData)
        }

    }

    //
    private fun writeRussianServiceToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/услуги")
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
        myRef.child("Услуги/LTE (4G)").setValue(service1)
        val service2 = NetData(
            "",
            "",
            "",
            "",
            "",
            "УСЛУГА 'Restart'",
            "",
            "*147*10043*28585#"
        )
        myRef.child("Услуги/УСЛУГА 'Restart'").setValue(service2)

        val service3 = NetData(
            "",
            "",
            "",
            "",
            "",
            "ГОЛОСОВАЯ ПОЧТА",
            "",
            "*147*10043*28585#"
        )
        myRef.child("Услуги/ГОЛОСОВАЯ ПОЧТА").setValue(service3)

        val service4 = NetData(
            "",
            "",
            "",
            "",
            "",
            "ПО МИРУ",
            "",
            "*147*10043*28585#"
        )
        myRef.child("Роуминг/ПО МИРУ").setValue(service4)

        val service5 = NetData(
            "",
            "",
            "",
            "",
            "",
            "ПОЛЕТЕ",
            "",
            "*147*10043*28585#"
        )
        myRef.child("Роуминг/ПОЛЕТЕ").setValue(service5)

    }

    //
    private fun writeRussianInternetToFirebase() {
        val database = FirebaseDatabase.getInstance(URL)
        val myRef =
            database.getReference("$FIREBASE_PACKAGE/$LAN_RU/интернет")

        for (i in 1..5) {
            val netData = NetData(
                "${i}000 сумов",
                "1 день",
                "${i}00 MB",
                "",
                "",
                "ДНЕВНЫЙ ${i}00 MB",
                "${i}00",
                "*147*10043*28585#"
            )
            myRef.child("Ежедневные пакеты/ДНЕВНЫЙ ${i}00 MB").setValue(netData)
        }

        for (i in 1..5) {
            val netData = NetData(
                "${i}0000 сумов",
                "30 дней",
                "${i}0 GB",
                "",
                "",
                "${i}0 GB",
                "${i}0 GB",
                "*147*10043*28585#"
            )
            myRef.child("Ежемесячные пакеты/${i}0 GB").setValue(netData)
        }

        for (i in 5..15 step 3) {
            val netData = NetData(
                "${i}0000 сумов",
                "30 дней",
                "${i}0 GB",
                "",
                "",
                "${i}0 GB",
                "${i}0",
                "*147*10043*28585#"
            )
            myRef.child("Интернет non-stop/${i}0 GB").setValue(netData)
        }

        val netData1 = NetData(
            "6300 сумов",
            "1 ночь",
            "12 000 MB",
            "",
            "",
            "НОЧЬ",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("Ночной интернет/НОЧ").setValue(netData1)

        val netData2 = NetData(
            "31 500 сумов",
            "7 ночей",
            "12 000 MB",
            "",
            "",
            "7 НОЧЕЙ",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("Ночной интернет/7 НОЧЕЙ").setValue(netData2)

        val netData3 = NetData(
            "99 000 сумов",
            "30 ночей",
            "12 000 MB",
            "",
            "",
            "30 НОЧЕЙ",
            "12 GB",
            "*147*10043*28585#"
        )
        myRef.child("Ночной интернет/30 НОЧЕЙ").setValue(netData3)
    }
}