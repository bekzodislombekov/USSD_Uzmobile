package com.android.example.ussduzmobile.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.adapters.MainTariffAdapter
import com.android.example.ussduzmobile.adapters.OnClickItemListener
import com.android.example.ussduzmobile.databinding.FragmentHomeBinding
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.ui.home.lists.ListFragment
import com.android.example.ussduzmobile.ui.home.services.NetFragment
import com.android.example.ussduzmobile.utils.*
import com.google.firebase.database.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var lan: String? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var list: ArrayList<TariffData>
    private lateinit var adapter: MainTariffAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var pos = 0
    private var tariffHome = ""
    private var netPack = ""
    private var minPack = ""
    private var messPack = ""
    private var tariff = ""
    private var service = ""
    private var ussd = ""
    private val TAG = "HomeFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            lan = it.getString(ARG_PARAM2)
        }
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tgBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/uzmobile_uzmobil_internet_paket")
            )
            activity?.startActivity(intent)
        }
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nUSSD Uzmobile\n"
            shareMessage =
                "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            activity?.startActivity(Intent.createChooser(shareIntent, "Choose one"))
        }

        binding.apply {
            if (lan == LAN_RU) {
                slogan.setText(R.string.slogan_ru)
                ussdTv.setText(R.string.ussd_title_ru)
                tariffTv.setText(R.string.tariff_title_ru)
                serviceTv.setText(R.string.service_title_ru)
                minuteTv.setText(R.string.minute_title_ru)
                internetTv.setText(R.string.internet_title_ru)
                messageTv.setText(R.string.message_title_ru)
            } else if (lan == LAN_UZ_CIRIL) {
                slogan.setText(R.string.slogan_uz)
                ussdTv.setText(R.string.ussd_title_uz)
                tariffTv.setText(R.string.tariff_title_uz)
                serviceTv.setText(R.string.service_title_uz)
                minuteTv.setText(R.string.minute_title_uz)
                internetTv.setText(R.string.internet_title_uz)
                messageTv.setText(R.string.message_title_uz)
            }
        }

        val i = requireActivity().supportFragmentManager.backStackEntryCount - 1
        Log.d(TAG, "onCreateView: backStackCount = $i")

        when (lan) {
            LAN_UZ_LATIN -> {
                netPack = "internet"
                minPack = "daqiqa"
                messPack = "sms"
                tariff = "tariff"
                service = "xizmatlar"
                ussd = "ussd"
                tariff = "tariff"
                tariffHome = "tariff"
            }
            LAN_RU -> {
                netPack = "интернет"
                minPack = "минут"
                messPack = "смс"
                tariff = "тариф"
                service = "услуги"
                ussd = "ussd"
                tariffHome = "тариф"
            }
            LAN_UZ_CIRIL -> {
                netPack = "интернет"
                minPack = "минут"
                messPack = "смс"
                tariff = "тариф"
                service = "хизматлар"
                ussd = "ussd"
                tariffHome = "тариф"
            }
        }

        database = FirebaseDatabase.getInstance(URL)
        reference = database.getReference("$FIREBASE_LIST/$lan")

        reference.child(tariffHome).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                list = ArrayList()
                for (child in children) {
                    val value = child.getValue(TariffData::class.java)
                    list.add(value!!)
                }
                adapter = MainTariffAdapter(lan!!, list, object : OnClickItemListener {
                    override fun onClickListener(tariffData: TariffData) {
                        val bundle = bundleOf(TARIFF_TEXT to tariffData)
                        requireActivity().supportFragmentManager.commit {
                            setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                            )
                            add(
                                R.id.frameLayoutFragment,
                                TariffFragment::class.java,
                                bundle
                            )
                            addToBackStack(null)
                        }
                    }
                })
                binding.rv.adapter = adapter
                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(binding.rv)
                binding.circleIndicator.attachToRecyclerView(binding.rv, pagerSnapHelper)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.netBtn.setOnClickListener {
            sendData(NetFragment(), netPack)
        }
        binding.minuteBtn.setOnClickListener {
            sendData(NetFragment(), minPack)
        }
        binding.messageBtn.setOnClickListener {
            sendData(NetFragment(), messPack)
        }
        binding.serviceBtn.setOnClickListener {
            sendData(NetFragment(), service)
        }
        binding.tariffBtn.setOnClickListener {
            sendData(ListFragment(), tariff)
        }
        binding.codeBtn.setOnClickListener {
            sendData(ListFragment(), ussd)
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rv.layoutManager = linearLayoutManager

        val mHandler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding.rv.smoothScrollToPosition(pos)
                pos++
                mHandler.postDelayed(this, 3000)
            }
        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem: Int = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val currentPos = linearLayoutManager.findFirstVisibleItemPosition()
                pos = currentPos
                if (lastItem == linearLayoutManager.itemCount - 1) {
                    mHandler.removeCallbacks(runnable)
                    val postHandler = Handler(Looper.myLooper()!!)
                    pos = 0
                    postHandler.postDelayed({
                        binding.rv.smoothScrollToPosition(pos)
                        mHandler.postDelayed(runnable, 3000)
                    }, 3000)
                }
            }
        })
        mHandler.postDelayed(runnable, 100)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sendData(fragment: Fragment, value: String) {
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            add(
                R.id.frameLayoutFragment,
                fragment::class.java,
                bundleOf(PACKAGE_TEXT to value)
            )
            addToBackStack(null)
        }
    }
}