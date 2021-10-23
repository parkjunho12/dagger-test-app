package co.kr.imageapp.kakao.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import co.kr.imageapp.kakao.databinding.ActivityMainBinding
import co.kr.imageapp.kakao.ui.base.BaseActivity
import co.kr.imageapp.kakao.ui.main.adapter.MainFragmentAdapter
import co.kr.imageapp.kakao.ui.main.fragment.mypage.MyPageFragment
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        setViewPager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun selectMyPage() {
        mainBinding.viewpagerContainer.setCurrentItem(1, true)
    }

    private fun setViewPager()= with(mainBinding) {
        val searchFragment = SearchFragment.newInstance()
        val mypageFragment = MyPageFragment.newInstance()
        val tabTitles = arrayOf("검색", "내 보관함")
        val adapter = MainFragmentAdapter(this@MainActivity)
        adapter.addFrag(searchFragment)
        adapter.addFrag(mypageFragment)
        viewpagerContainer.adapter = adapter
        viewpagerContainer.offscreenPageLimit = 3
        viewpagerContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> {

                    }
                    2 -> {

                    }
                    else -> {

                    }
                }
            }
        })

        TabLayoutMediator(mainTablayout, viewpagerContainer) { tab, position ->
            tab.text = tabTitles[position]
            viewpagerContainer.setCurrentItem(tab.position, true)
        }.attach()
    }
}