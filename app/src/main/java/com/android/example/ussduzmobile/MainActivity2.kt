package com.android.example.ussduzmobile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.example.ussduzmobile.databinding.ActivityMain2Binding
import com.android.example.ussduzmobile.ui.NotificationFragment
import com.android.example.ussduzmobile.ui.home.HomeFragment
import com.android.example.ussduzmobile.ui.home.lists.ListFragment
import com.android.example.ussduzmobile.utils.LAN_KEY
import com.android.example.ussduzmobile.utils.LAN_RU
import com.android.example.ussduzmobile.utils.LAN_UZ_CIRIL
import com.android.example.ussduzmobile.utils.SHARED_PREF_NAME
import com.google.android.material.navigation.NavigationView


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private var currentTab = ""
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        language = sharedPreferences.getString(LAN_KEY, "")!!

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.title = "USSD"

        binding.appBarMain.contentMain.apply {
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

//        val navController = findNavController(R.id.frameLayoutFragment1)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
//            ), drawerLayout
//        )

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener {


            return@setNavigationItemSelectedListener true
        }
//        setupActionBarWithNavController(navController, drawerLayout)
//        navView.setupWithNavController(navController)

        setListener()
        changeTab(MainActivity.HOME_TAB)
    }

    private fun setListener() {
        binding.appBarMain.contentMain.apply {
            homeBtn.setOnClickListener {
                changeTab(MainActivity.HOME_TAB)
            }
            linearBalance.setOnClickListener {
                changeTab(MainActivity.BALANCE_TAB)
            }
            linearOperator.setOnClickListener {
                changeTab(MainActivity.OPERATOR_TAB)
            }
            linearNotification.setOnClickListener {
                changeTab(MainActivity.NOTIFICATION_TAB)
            }
            linearSettings.setOnClickListener {
                changeTab(MainActivity.SETTINGS_TAB)
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
            MainActivity.HOME_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()

                when (currentFragment) {
                    null -> {
                        currentFragment = HomeFragment.newInstance("index 0", language)
                        addFragment(currentFragment, MainActivity.HOME_TAB)
                    }
                    else -> {
                        currentFragment.childFragmentManager
                        showFragment(currentFragment)
                    }
                }
            }

            MainActivity.BALANCE_TAB -> {
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
            MainActivity.OPERATOR_TAB -> {
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
                    addFragment(currentFragment, MainActivity.OPERATOR_TAB)
                } else {
                    showFragment(currentFragment)
                }
            }
            MainActivity.NOTIFICATION_TAB -> {
                currentTab = selectedTab
                currentFragment = supportFragmentManager.findFragmentByTag(currentTab)

                resetTab()

                if (currentFragment == null) {
                    currentFragment = NotificationFragment.newInstance("index 3")
                    addFragment(currentFragment, MainActivity.NOTIFICATION_TAB)
                } else {
                    showFragment(currentFragment)
                }
            }
            MainActivity.SETTINGS_TAB -> {
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
        var fragment = supportFragmentManager.findFragmentByTag(MainActivity.HOME_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(MainActivity.BALANCE_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(MainActivity.OPERATOR_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(MainActivity.NOTIFICATION_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

        fragment = supportFragmentManager.findFragmentByTag(MainActivity.SETTINGS_TAB)
        if (fragment != null) {
            hideFragment(fragment)
        }

    }

    private fun resetNavigationBarButton() {
//        binding.appBarMain.contentMain.apply {
//            linearBalance.setBackgroundColor(getColor(R.color.uzmobile3))
//            linearOperator.setBackgroundColor(getColor(R.color.uzmobile3))
//            linearNotification.setBackgroundColor(getColor(R.color.uzmobile3))
//            linearSettings.setBackgroundColor(getColor(R.color.uzmobile3))
//        }
    }

    private fun updateNavigationBarButton() {

//        when (currentTab) {
//            MainActivity.BALANCE_TAB -> {
//                binding.linearBalance.setBackgroundColor(getColor(R.color.white))
//            }
//            MainActivity.OPERATOR_TAB -> {
//                binding.linearOperator.setBackgroundColor(getColor(R.color.white))
//            }
//            MainActivity.NOTIFICATION_TAB -> {
//                binding.linearNotification.setBackgroundColor(getColor(R.color.white))
//            }
//            MainActivity.SETTINGS_TAB -> {
//                binding.linearSettings.setBackgroundColor(getColor(R.color.white))
//            }
//        }
    }


    private fun addFragment(fragment: Fragment, tag: String) {
        if (fragment.isAdded) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(R.id.frameLayoutFragment1, fragment, tag).commitAllowingStateLoss()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
    }
}