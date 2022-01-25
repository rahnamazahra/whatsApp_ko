package com.rahnama.whatsapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ManageViewPagerAdapter(manager: FragmentManager) :FragmentPagerAdapter(manager){
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
      when(position){
          0->return Usersfragment()
          1->return Chatsfragment()
      }
        return  null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->return "مخاطبین"
            1->return "چت"
        }
        return null!!
    }

}