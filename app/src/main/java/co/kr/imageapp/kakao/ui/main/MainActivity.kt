package co.kr.imageapp.kakao.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.databinding.ActivityMainBinding
import co.kr.imageapp.kakao.ui.base.BaseActivity
import co.kr.imageapp.kakao.ui.main.adapter.MainFragmentAdapter
import co.kr.imageapp.kakao.ui.main.fragment.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        setViewPager()
    }

    private fun setViewPager()= with(mainBinding) {
        val searchFragment = SearchFragment.newInstance()
        val searchFragment2 = SearchFragment.newInstance()
        val tabTitles = arrayOf("검색", "검색2")
        val adapter = MainFragmentAdapter(this@MainActivity)
        adapter.addFrag(searchFragment)
        adapter.addFrag(searchFragment2)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}