package com.android.example.ussduzmobile.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.transition.TransitionInflater
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.databinding.FragmentTariffBinding
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.utils.*

private const val ARG_PARAM1 = TARIFF_TEXT

class TariffFragment : Fragment() {
    private var param1: TariffData? = null
    private lateinit var binding: FragmentTariffBinding
    private lateinit var transitionInf: TransitionInflater
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as TariffData?
        }
        transitionInf = TransitionInflater.from(requireContext())
        enterTransition = transitionInf.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTariffBinding.inflate(inflater, container, false)


        val sharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        language = sharedPreferences.getString(LAN_KEY, "")!!

        when (language) {
            LAN_RU -> {
                binding.apply {
                    param1?.apply {
                        binding.payment.text = "В месяц $payment"
                    }
                    tariffTv.text = "Тариф"
                    payTv.text = "Абонентская плата"
                    aboutBtn.text = "Более"
                    connectBtn.text = "Активация"
                }
            }
            LAN_UZ_CIRIL -> {
                binding.apply {
                    param1?.apply {
                        binding.payment.text = "Ойига $payment"
                    }
                    tariffTv.text = "Тариф"
                    payTv.text = "Aбонент тўлови"
                    aboutBtn.text = "Батафсил"
                    connectBtn.text = "Уланиш"
                }
            }
        }

        binding.apply {
            param1?.apply {
                nameTariff.text = mainName
                phone.text = minute
                internet.text = traffic
                messageTv.text = message
                nameTariff2.text = "'${mainName}'"
                payment2.text = payment
            }
            backBtn.setOnClickListener {
                val manager = requireActivity().supportFragmentManager
                manager.popBackStack()
            }
            connectBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.fromParts("tel", param1?.toActivate, null)
                activity?.startActivity(intent)
            }
        }
        return binding.root
    }
}