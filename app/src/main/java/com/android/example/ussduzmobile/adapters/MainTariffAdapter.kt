package com.android.example.ussduzmobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.ussduzmobile.databinding.ItemTariffBinding
import com.android.example.ussduzmobile.models.TariffData
import com.android.example.ussduzmobile.utils.LAN_RU
import com.android.example.ussduzmobile.utils.LAN_UZ_CIRIL

class MainTariffAdapter(
    private val lan: String,
    private val list: List<TariffData>,
    val onClickItemListener: OnClickItemListener
) : RecyclerView.Adapter<MainTariffAdapter.VH>() {

    inner class VH(private val itemTariffBinding: ItemTariffBinding) :
        RecyclerView.ViewHolder(itemTariffBinding.root) {
        fun onBind(tariffData: TariffData) {
            itemTariffBinding.apply {
                nameTariff.text = tariffData.mainName
                phone.text = tariffData.minute
                internet.text = tariffData.traffic
                message.text = tariffData.message
                when (lan) {
                    LAN_RU -> {
                        payment.text = "В месяц ${tariffData.payment}"
                    }
                    LAN_UZ_CIRIL -> {
                        payment.text = "Ойига ${tariffData.payment}"
                    }
                    else -> {
                        payment.text = "Oyiga ${tariffData.payment}"
                    }
                }

                container.setOnClickListener {
                    onClickItemListener.onClickListener(tariffData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemTariffBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size
}

interface OnClickItemListener {
    fun onClickListener(tariffData: TariffData)
}