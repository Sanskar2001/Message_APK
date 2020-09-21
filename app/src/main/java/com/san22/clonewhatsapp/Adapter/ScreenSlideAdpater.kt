package com.san22.clonewhatsapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.san22.clonewhatsapp.Fragments.PeopleFragment
import com.san22.clonewhatsapp.Fragments.ChatsFragment

class ScreenSlideAdpater(Fragment:FragmentActivity):FragmentStateAdapter(Fragment)
{

    override fun getItemCount()=2



    override fun createFragment(position: Int): Fragment=when(position){
            0->ChatsFragment()
            else->PeopleFragment()
    }


}