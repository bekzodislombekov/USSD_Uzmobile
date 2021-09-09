package com.android.example.ussduzmobile.ui.home.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.transition.TransitionInflater
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.adapters.ViewPagerAdapter
import com.android.example.ussduzmobile.databinding.FragmentNetBinding
import com.android.example.ussduzmobile.databinding.ItemTabBinding
import com.android.example.ussduzmobile.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source

class NetFragment : Fragment() {
    private lateinit var binding: FragmentNetBinding
    private lateinit var packageList: ArrayList<String>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val TAG = "NetFragment"
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var packages: String
    private var title = ""
    private var lan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packages = arguments?.getString(PACKAGE_TEXT) ?: ""
        val transitionInf = TransitionInflater.from(requireContext())
        enterTransition = transitionInf.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNetBinding.inflate(inflater, container, false)
        val i = requireActivity().supportFragmentManager.backStackEntryCount - 1
        Log.d(TAG, "onCreateView: backStackCount = $i")
        val sharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        lan = sharedPreferences.getString(LAN_KEY, "")!!

        binding.apply {
            when (lan) {
                LAN_RU -> {
                    when (packages) {
                        "интернет" -> {
                            checkNet.setText(R.string.check_internet_btn_ru)
                            title = getString(R.string.internet_title_ru)
                            titleTv.text = "$title пакеты"
                        }
                        "минут" -> {
                            checkNet.setText(R.string.check_minute_btn_ru)
                            title = getString(R.string.minute_title_ru)
                            titleTv.text = "$title пакеты"
                        }
                        "смс" -> {
                            checkNet.setText(R.string.check_message_btn_ru)
                            title = getString(R.string.message_title_ru)
                            titleTv.text = "$title пакеты"
                        }
                        "услуги" -> {
                            checkNet.visibility = View.GONE
                            title = getString(R.string.service_title_ru)
                            titleTv.text = title
                        }
                    }
                }
                LAN_UZ_CIRIL -> {
                    when (packages) {
                        "интернет" -> {
                            checkNet.setText(R.string.check_internet_btn_uz)
                            title = getString(R.string.internet_title_uz)
                            titleTv.text = "$title пакетлар"
                        }
                        "минут" -> {
                            checkNet.setText(R.string.check_minute_btn_uz)
                            title = getString(R.string.minute_title_uz)
                            titleTv.text = "$title пакетлар"
                        }
                        "смс" -> {
                            checkNet.setText(R.string.check_message_btn_uz)
                            title = getString(R.string.message_title_uz)
                            titleTv.text = "$title пакетлар"
                        }
                        "хизматлар" -> {
                            checkNet.visibility = View.GONE
                            title = getString(R.string.service_title_uz)
                            titleTv.text = title
                        }
                    }
                }
                else -> {
                    when (packages) {
                        "internet" -> {
                            binding.checkNet.setText(R.string.check_internet_btn)
                            title = getString(R.string.internet_title)
                            titleTv.text = "$title paketlar"
                        }
                        "daqiqa" -> {
                            binding.checkNet.setText(R.string.check_minute_btn)
                            title = getString(R.string.minute_title)
                            titleTv.text = "$title paketlar"
                        }
                        "sms" -> {
                            binding.checkNet.setText(R.string.check_message_btn)
                            title = getString(R.string.message_title)
                            titleTv.text = "$title paketlar"
                        }
                        "xizmatlar" -> {
                            binding.checkNet.visibility = View.GONE
                            title = getString(R.string.service_title)
                            titleTv.text = title
                        }
                    }
                }
            }
        }

        binding.backBtn.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            manager.popBackStack()
        }
        binding.checkNet.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.fromParts("tel", "*100*2#", null)
            activity?.startActivity(intent)
        }

        database = FirebaseDatabase.getInstance(URL)
        reference = database.getReference("$FIREBASE_PACKAGE/$lan")

        /*reference = if (lan == LAN_RU) {
            database.getReference("$FIREBASE_PACKAGE/$lan")
        } else if (lan == LAN_UZ_CIRIL){
            database.getReference(FIREBASE_PACKAGE)
        }else{
            database.getReference(FIREBASE_PACKAGE)
        }*/

        reference.child(packages).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                packageList = ArrayList()
                for (child in children) {
                    packageList.add(child.key!!)
                }
                binding.progressCircular.visibility = View.GONE
                viewPagerAdapter = ViewPagerAdapter(requireActivity(), packageList, packages)
                binding.viewPager.adapter = viewPagerAdapter
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = packageList[position]
                }.attach()
                setTabs()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: Error", error.toException())
            }

        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val tabCard = customView?.findViewById<MaterialCardView>(R.id.tab_card)
                val tabName = customView?.findViewById<TextView>(R.id.tab_name)
                tabName?.setTextColor(requireActivity().getColor(R.color.uzmobile))
                tabCard?.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val tabCard = customView?.findViewById<MaterialCardView>(R.id.tab_card)
                val tabName = customView?.findViewById<TextView>(R.id.tab_name)
                tabCard?.setCardBackgroundColor(requireActivity().getColor(R.color.uzmobile))
                tabName?.setTextColor(requireActivity().getColor(R.color.white))
                tabCard?.strokeColor = requireActivity().getColor(R.color.white)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return binding.root
    }

    private fun setTabs() {
        val count = binding.tabLayout.tabCount
        for (i in 0 until count) {
            val bindingTab = ItemTabBinding.inflate(layoutInflater)
            val tabName = bindingTab.tabName
            val tabCard = bindingTab.tabCard
            tabName.text = packageList[i]
            if (i == 0) {
                tabName.setTextColor(requireActivity().getColor(R.color.uzmobile))
                tabCard.setCardBackgroundColor(requireActivity().getColor(R.color.white))
            } else {
                tabCard.setCardBackgroundColor(requireActivity().getColor(R.color.uzmobile))
                tabName.setTextColor(requireActivity().getColor(R.color.white))
                tabCard.strokeColor = requireActivity().getColor(R.color.white)
            }
            binding.tabLayout.getTabAt(i)?.customView = bindingTab.root
        }
    }
}