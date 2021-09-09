package com.android.example.ussduzmobile.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.databinding.ItemServicesBinding
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.utils.LAN_RU
import com.android.example.ussduzmobile.utils.LAN_UZ_CIRIL

class ListAdapter(
    private val packages: String,
    private val lan: String,
    private val list: List<TariffData>,
    private val listener: OnClickListener
) :
    RecyclerView.Adapter<ListAdapter.Vh>() {

    inner class Vh(private val itemServicesBinding: ItemServicesBinding) :
        RecyclerView.ViewHolder(itemServicesBinding.root) {
        fun onBind(tariffData: TariffData, position: Int) {
            itemServicesBinding.namePackage.text = tariffData.mainName

            when (lan) {
                LAN_RU -> {
                    itemServicesBinding.paymentTv.text = "Абонентская плата: ${tariffData.payment}"
                    itemServicesBinding.minuteTv.text =
                        "Исходящие по Узбекистану: ${tariffData.minute}"
                    itemServicesBinding.netTv.text = "Мобильный интернет: ${tariffData.traffic}"
                }
                LAN_UZ_CIRIL -> {
                    itemServicesBinding.paymentTv.text = "Aбонент тўлови: ${tariffData.payment}"
                    itemServicesBinding.minuteTv.text = "Ўзбекистион бўйича: ${tariffData.minute}"
                    itemServicesBinding.netTv.text = "Мобил интернет: ${tariffData.traffic}"
                }
                else -> {
                    itemServicesBinding.paymentTv.text = "Abonent to'lovi: ${tariffData.payment}"
                    itemServicesBinding.minuteTv.text = "O'zbekiston bo'yicha: ${tariffData.minute}"
                    itemServicesBinding.netTv.text = "Mobil internet: ${tariffData.traffic}"
                }
            }
            itemServicesBinding.connectBtn.setOnClickListener {
                listener.onListener(tariffData)
            }

            if (packages == "ussd") {
                itemServicesBinding.paymentTv.text = ""
                itemServicesBinding.minuteTv.visibility = View.GONE
                itemServicesBinding.minuteTv.text = ""
                itemServicesBinding.netTv.text = ""
            }
            if (packages == "operator" || packages == "оператор") {
                itemServicesBinding.paymentTv.text = ""
                itemServicesBinding.connectBtn.visibility = View.INVISIBLE
                itemServicesBinding.minuteTv.visibility = View.GONE
                itemServicesBinding.netTv.visibility = View.GONE
                itemServicesBinding.minuteTv.text = ""
                itemServicesBinding.netTv.text = ""
            }
            if (tariffData.name == "") {
                itemServicesBinding.logo.visibility = View.VISIBLE
                itemServicesBinding.name.visibility = View.GONE
            }
            if (position == list.size - 1) {
                itemServicesBinding.divider.visibility = View.INVISIBLE
            }
            if (itemServicesBinding.expandableTv.isExpanded) {
                itemServicesBinding.more.setImageResource(R.drawable.ic_arrow_up)
            } else {
                itemServicesBinding.more.setImageResource(R.drawable.ic_arrow_down)
            }
            itemServicesBinding.more.setOnClickListener {
                itemServicesBinding.expandableTv.toggle(true)
            }
            if (itemServicesBinding.expandableTv.isExpanded) {
                itemServicesBinding.more.setImageResource(R.drawable.ic_arrow_up)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount() = list.size
}

interface OnClickListener {
    fun onListener(tariffData: TariffData)
}