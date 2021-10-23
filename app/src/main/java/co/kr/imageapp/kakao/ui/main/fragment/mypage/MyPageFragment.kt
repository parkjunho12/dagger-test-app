package co.kr.imageapp.kakao.ui.main.fragment.mypage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.error.DEFAULT_ERROR
import co.kr.imageapp.kakao.data.error.NETWORK_ERROR
import co.kr.imageapp.kakao.data.error.NO_INTERNET_CONNECTION
import co.kr.imageapp.kakao.data.error.SEARCH_ERROR
import co.kr.imageapp.kakao.databinding.MyPageFragmentBinding
import co.kr.imageapp.kakao.ui.main.fragment.mypage.adapter.MyPageAdapter
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchViewModel
import co.kr.imageapp.kakao.ui.main.fragment.search.adapter.SearchAdapter
import co.kr.imageapp.kakao.util.observe
import co.kr.imageapp.kakao.util.sortDateTime
import co.kr.imageapp.kakao.util.toGone
import co.kr.imageapp.kakao.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.my_page_fragment.*


@AndroidEntryPoint
class MyPageFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var viewModel: MyPageViewModel
    private lateinit var myPageBinding: MyPageFragmentBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var myPageAdapter: MyPageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPageBinding = MyPageFragmentBinding.inflate(layoutInflater)
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        myPageBinding.recyclerviewMyPage.layoutManager = gridLayoutManager
        myPageBinding.recyclerviewMyPage.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        observeViewModel()
        return myPageBinding.root
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated(){
        Log.i("tag","reached the State.Created")
        viewModel.getMyImages()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }

    private fun showLoadingView() = with(myPageBinding) {
        cicleProgressMyPage.toVisible()
        recyclerviewMyPage.toGone()
        tvNoDataMyPage.toGone()

    }

    private fun showDataView(show: Boolean) = with(myPageBinding) {
        tvNoDataMyPage.visibility = if (show) GONE else VISIBLE
        recyclerviewMyPage.visibility = if (show) VISIBLE else GONE
        cicleProgressMyPage.toGone()
    }

    private fun bindImageListData(imageList: List<ImageData>) = with(myPageBinding) {
        if (!(imageList.isNullOrEmpty())) {
            myPageAdapter = MyPageAdapter(viewModel, imageList)
            recyclerviewMyPage.adapter = myPageAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun handleMyImages(status: Resource<List<ImageData>>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let {
                bindImageListData(it)
            }
            is Resource.LastSuccess -> status.data?.let {
                bindImageListData(it)
            }
            is Resource.DataError -> {
                if (status.errorCode == SEARCH_ERROR) {
                    showDataView(false)
                    returnErrorCode(status.errorCode).let { viewModel.showToastMessage(it) }
                } else {
                    showDataView(false)
                    returnErrorCode(status.errorCode).let { viewModel.showToastMessage(it) }
                }
            }
        }
    }


    private fun observeViewModel() {
        observe(viewModel.imageLiveData, ::handleMyImages)
    }

    private fun returnErrorCode(errorCode: Int?): String {
        return when (errorCode) {
            NO_INTERNET_CONNECTION -> {
                "인터넷 연결 안됨"
            }
            NETWORK_ERROR -> {
                "네트워크 에러"
            }
            DEFAULT_ERROR -> {
                "기본 에러"
            }
            SEARCH_ERROR -> {
                "검색 마지막 결과입니다."
            }
            else -> ""
        }
    }
}