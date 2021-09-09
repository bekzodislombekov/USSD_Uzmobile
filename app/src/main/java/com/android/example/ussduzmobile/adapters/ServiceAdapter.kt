package com.android.example.ussduzmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.databinding.ItemServicesBinding
import com.android.example.ussduzmobile.models.NetData
import com.android.example.ussduzmobile.utils.LAN_RU
import com.android.example.ussduzmobile.utils.LAN_UZ_CIRIL

class ServiceAdapter(
    private val lan: String,
    private val packages: String,
    private val list: List<NetData>
) :
    RecyclerView.Adapter<ServiceAdapter.Vh>() {

    inner class Vh(private val itemServicesBinding: ItemServicesBinding) :
        RecyclerView.ViewHolder(itemServicesBinding.root) {
        fun onBind(netData: NetData, position: Int) {
            itemServicesBinding.namePackage.text = netData.mainName
            itemServicesBinding.name.text = netData.name

            when (lan) {
                LAN_RU -> {
                    itemServicesBinding.paymentTv.text =
                        "Абонентская плата: ${netData.payment}"
                    when (packages) {
                        "интернет" -> {
                            itemServicesBinding.netTv.text = "Трафик: ${netData.traffic}"
                        }
                        "минут" -> {
                            itemServicesBinding.netTv.text = "Минуты в пакете: ${netData.minute}"
                        }
                        "смс" -> {
                            itemServicesBinding.netTv.text = "СМС в пакете: ${netData.message}"
                        }
                    }
                    itemServicesBinding.minuteTv.text = "Срок годности: ${netData.period}"

                }
                LAN_UZ_CIRIL -> {
                    itemServicesBinding.paymentTv.text =
                        "Aбонент тўлови: ${netData.payment}"
                    when (packages) {
                        "интернет" -> {
                            itemServicesBinding.netTv.text = "Трафик: ${netData.traffic}"
                        }
                        "минут" -> {
                            itemServicesBinding.netTv.text =
                                "Берилган дақиқалар: ${netData.minute}"
                        }
                        "смс" -> {
                            itemServicesBinding.netTv.text = "Берилган СМС лар: ${netData.message}"
                        }
                    }
                    itemServicesBinding.minuteTv.text = "Aмал қилиш муддати: ${netData.period}"
                }
                else -> {
                    itemServicesBinding.paymentTv.text =
                        "Abonent to'lovi: ${netData.payment}"
                    when (packages) {
                        "internet" -> {
                            itemServicesBinding.netTv.text = "Trafik: ${netData.traffic}"
                        }
                        "daqiqa" -> {
                            itemServicesBinding.netTv.text =
                                "Berilgan daqiqalar: ${netData.minute}"
                        }
                        "sms" -> {
                            itemServicesBinding.netTv.text = "Berilgan SMS lar: ${netData.message}"
                        }
                    }
                    itemServicesBinding.minuteTv.text = "Amal qilish muddati: ${netData.period}"
                }
            }

            if (packages == "xizmatlar" || packages == "хизматлар" || packages == "услуги") {
                itemServicesBinding.minuteTv.visibility = View.GONE
                itemServicesBinding.paymentTv.text = ""
                itemServicesBinding.netTv.text = ""
            }

            if (netData.name == "") {
                itemServicesBinding.name.visibility = View.GONE
                itemServicesBinding.logo.visibility = View.VISIBLE
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
            itemServicesBinding.container.setOnClickListener {
                itemServicesBinding.expandableTv.toggle(true)
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