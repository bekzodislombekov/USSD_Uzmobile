package com.android.example.ussduzmobile.ui.home.lists

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.adapters.ListAdapter
import com.android.example.ussduzmobile.adapters.OnClickListener
import com.android.example.ussduzmobile.databinding.FragmentListBinding
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.utils.*
import com.google.firebase.database.*

private const val ARG_PARAM1 = PACKAGE_TEXT

class ListFragment : Fragment() {
    private var param1: String? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var adapter: ListAdapter
    private lateinit var list: ArrayList<TariffData>
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(PACKAGE_TEXT)
            val transitionInf = TransitionInflater.from(requireContext())
            enterTransition = transitionInf.inflateTransition(R.transition.slide_right)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        val sharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        language = sharedPreferences.getString(LAN_KEY, "")!!

        binding.backBtn.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            manager.popBackStack()
        }

        when (language) {
            LAN_RU -> {
                when (param1) {
                    "тариф" -> {
                        binding.checkNet.text = getString(R.string.check_tariff_btn_ru)
                        binding.title.text = getString(R.string.tariff_title_ru)
                    }
                    "ussd" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.ussd_title_ru)
                    }
                    "оператор" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.connect_with_operator_ru)
                        binding.backBtn.visibility = View.GONE
                    }
                }
            }
            LAN_UZ_CIRIL -> {
                when (param1) {
                    "тариф" -> {
                        binding.checkNet.text = getString(R.string.check_tariff_btn_uz)
                        binding.title.text = getString(R.string.check_tariff_btn_uz)
                    }
                    "ussd" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.ussd_title_uz)
                    }
                    "оператор" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.connect_with_operator_uz)
                        binding.backBtn.visibility = View.GONE
                    }
                }
            }
            else -> {
                when (param1) {
                    "tariff" -> {
                        binding.checkNet.text = getString(R.string.check_tariff_btn)
                        binding.title.text = getString(R.string.check_tariff_btn)
                    }
                    "ussd" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.ussd_title)
                    }
                    "operator" -> {
                        binding.checkNet.visibility = View.GONE
                        binding.title.text = getString(R.string.connect_with_operator)
                        binding.backBtn.visibility = View.GONE
                    }
                }
            }
        }

        database = FirebaseDatabase.getInstance(URL)
        reference = database.getReference("$FIREBASE_LIST/$language")

        reference.child(param1!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                list = ArrayList()
                for (child in children) {
                    val value = child.getValue(TariffData::class.java)
                    list.add(value!!)
                }
                adapter = ListAdapter(param1!!, language, list, object : OnClickListener {
                    override fun onListener(tariffData: TariffData) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.fromParts("tel", tariffData.toActivate, null)
                        activity?.startActivity(intent)
                    }

                })
                binding.rv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}