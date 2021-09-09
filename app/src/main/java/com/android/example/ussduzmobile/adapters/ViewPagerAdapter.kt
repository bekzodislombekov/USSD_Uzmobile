package com.android.example.ussduzmobile.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.example.ussduzmobile.ui.home.services.PagerFragment

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val list: List<String>,
    private val value: String
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = list.size

    override fun createFragment(position: Int): Fragment {
        return PagerFragment.newInstance(list[position], value)
    }
}