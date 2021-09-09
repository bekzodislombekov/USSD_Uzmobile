package com.android.example.ussduzmobile.ui.home.services

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.example.ussduzmobile.adapters.ServiceAdapter
import com.android.example.ussduzmobile.databinding.FragmentPagerBinding
import com.android.example.ussduzmobile.models.NetData
import com.android.example.ussduzmobile.utils.*
import com.google.firebase.database.*

private const val ARG_PARAM1 = FIREBASE_PACKAGE
private const val ARG_PARAM2 = "value"

class PagerFragment : Fragment() {
    private var packages: String? = null
    private var value: String? = null
    private lateinit var binding: FragmentPagerBinding
    private lateinit var adapter: ServiceAdapter
    private lateinit var list: ArrayList<NetData>
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            packages = it.getString(ARG_PARAM1)
            value = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPagerBinding.inflate(inflater, container, false)
        val sharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val language = sharedPreferences.getString(LAN_KEY, "")!!
        database = FirebaseDatabase.getInstance(URL)
        reference = database.getReference("$FIREBASE_PACKAGE/$language")
//        reference = when (language) {
//            LAN_RU -> {
//                database.getReference("$FIREBASE_PACKAGE/$language")
//            }
//            LAN_UZ_CIRIL -> {
//                database.getReference("$FIREBASE_PACKAGE/$language")
//            }
//            else -> {
//                database.getReference(FIREBASE_PACKAGE)
//            }
//        }

        reference.child(value!!).child(packages!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    list = ArrayList()
                    for (child in children) {
                        val value = child.getValue(NetData::class.java)
                        list.add(value!!)
                    }
                    adapter = ServiceAdapter(language, value!!, list)
                    binding.rv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(packages: String, value: String) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, packages)
                    putString(ARG_PARAM2, value)
                }
            }
    }
}