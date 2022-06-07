package com.gp.shifa.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gp.shifa.data.models.IntroModel

class IntroPagerAdapter(fragmentActivity: FragmentActivity, datumList: List<IntroModel>) : FragmentStateAdapter(fragmentActivity) {
    private val total = datumList.size
    private val data: ArrayList<IntroFragment> = ArrayList(
            datumList.size
    )

    init {
        for (i in datumList.indices) {
            val fragment = IntroFragment()
            val bundle = Bundle()
            bundle.putInt("POSITION", i)
            fragment.arguments = bundle
            data.add(fragment)
        }
    }

    override fun getItemCount(): Int {
        return total
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }
}
