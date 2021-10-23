package co.kr.imageapp.kakao.ui.main.fragment.mypage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchViewModel

class MyPageFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var viewModel: MyPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_page_fragment, container, false)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated(){
        Log.i("tag","reached the State.Created")
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }

}