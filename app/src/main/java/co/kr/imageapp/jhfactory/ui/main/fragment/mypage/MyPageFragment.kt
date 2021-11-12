package co.kr.imageapp.jhfactory.ui.main.fragment.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.kr.imageapp.jhfactory.const.KeyConst.IMAGE_TYPE
import co.kr.imageapp.jhfactory.data.Resource
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.data.dto.search.SearchItem
import co.kr.imageapp.jhfactory.data.error.DEFAULT_ERROR
import co.kr.imageapp.jhfactory.data.error.NETWORK_ERROR
import co.kr.imageapp.jhfactory.data.error.NO_INTERNET_CONNECTION
import co.kr.imageapp.jhfactory.data.error.SEARCH_ERROR
import co.kr.imageapp.jhfactory.databinding.MyPageFragmentBinding
import co.kr.imageapp.jhfactory.ui.dialog.DialogPopup
import co.kr.imageapp.jhfactory.ui.main.fragment.mypage.adapter.MyPageAdapter
import co.kr.imageapp.jhfactory.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.my_page_fragment.*


@AndroidEntryPoint
class MyPageFragment : Fragment(), LifecycleObserver, DialogPopup.OnChoiceListener {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var viewModel: MyPageViewModel
    private lateinit var myPageBinding: MyPageFragmentBinding
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private lateinit var myPageAdapter: MyPageAdapter
    private var myPageList: List<ImageData>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPageBinding = MyPageFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        observeViewModel()
        setScrollListener()
        return myPageBinding.root
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun setScrollListener() = with(myPageBinding){
        recyclerviewMyPage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val visibleThreshold = 5
            private var previousTotalItemCount = 0
            private var currentPage = 0
            private var startingPageIndex = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var lastVisibleItemPosition = 0
                val layoutManager = recyclerviewMyPage.layoutManager
                val totalItemCount = layoutManager!!.itemCount



                val lastVisibleItems = (layoutManager as StaggeredGridLayoutManager)
                    .findLastCompletelyVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItems)

                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = this.startingPageIndex
                    this.previousTotalItemCount = totalItemCount
                }
                if (myPageList != null && lastVisibleItemPosition > -1 && lastVisibleItemPosition < myPageList!!.size) {
                    myPageTitle.text = myPageList!![lastVisibleItemPosition].regDT.split(",")[0]
                }
                if (cicleProgressMyPage.isVisible && (totalItemCount > previousTotalItemCount)) {
                    previousTotalItemCount = totalItemCount
                }

                if (!cicleProgressMyPage.isVisible && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    // 끝에 도착했을때 작업
                }
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated(){
        Log.i("tag","reached the State.Created")
        gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        myPageBinding.recyclerviewMyPage.layoutManager = gridLayoutManager
        myPageBinding.recyclerviewMyPage.setHasFixedSize(true)
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
            val myPageAdapter = MyPageAdapter(viewModel, imageList)
            myPageList = imageList
            recyclerviewMyPage.adapter = myPageAdapter
            recyclerviewMyPage.scrollToPosition(imageList.lastIndex)
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

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        myPageBinding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }


    private fun observeViewModel() {
        observe(viewModel.imageLiveData, ::handleMyImages)
        observe(viewModel.deleteMyImage, ::handleDeleteImage)
        observeToast(viewModel.showToast)
        observeEvent(viewModel.clickMyImage, ::popupItemDetail)
    }

    private fun popupItemDetail(clickEvent: SingleEvent<ImageData>) {
        clickEvent.getContentIfNotHandled()?.let {
            val imageData = clickEvent.peekContent()
            val searchItem = SearchItem(title = imageData.title, thumbnail_url = imageData.imageUri, image_url = imageData.imageUri,
            url = imageData.linkUrl, play_time = imageData.playTime, datetime = imageData.datetime, author = imageData.author, searchType = IMAGE_TYPE)
            val popupSearchClick = DialogPopup.newInstance(searchItem, true, requireContext())
            popupSearchClick.addChoiceListener(this)
            parentFragmentManager.beginTransaction().add(popupSearchClick, "DialogFragmnet Tag")
                .commitAllowingStateLoss()
        }
    }

    private fun handleDeleteImage(status: Resource<Boolean>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let {
                if(it) {
                    viewModel.getMyImages()
                }
            }
            is Resource.LastSuccess -> status.data?.let {
                if(it) {
                    viewModel.getMyImages()
                }
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

    override fun clickCancel() {

    }

    override fun clickOk(imageData: ImageData) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                imageData.linkUrl
            )
        )
        startActivity(browserIntent)
    }
}