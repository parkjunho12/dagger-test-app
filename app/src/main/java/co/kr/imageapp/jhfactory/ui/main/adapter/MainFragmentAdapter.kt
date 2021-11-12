package co.kr.imageapp.jhfactory.ui.main.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class MainFragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val mFragmentList = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment {

        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}