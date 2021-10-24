package co.kr.imageapp.kakao.ui.main.fragment.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.kr.imageapp.kakao.const.KeyConst.VIDEO_TYPE
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.error.DEFAULT_ERROR
import co.kr.imageapp.kakao.data.error.NETWORK_ERROR
import co.kr.imageapp.kakao.data.error.NO_INTERNET_CONNECTION
import co.kr.imageapp.kakao.data.error.SEARCH_ERROR
import co.kr.imageapp.kakao.databinding.SearchFragmentBinding
import co.kr.imageapp.kakao.ui.dialog.DialogPopup
import co.kr.imageapp.kakao.ui.main.MainActivity
import co.kr.imageapp.kakao.ui.main.fragment.search.adapter.SearchAdapter
import co.kr.imageapp.kakao.ui.main.fragment.search.adapter.SearchKeyAdapter
import co.kr.imageapp.kakao.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), LifecycleObserver, DialogPopup.OnChoiceListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchBinding: SearchFragmentBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchKeyAdapter: SearchKeyAdapter
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private lateinit var privateSearchItem: ArrayList<SearchItem>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var queryText = ""
    private var videoLast = false
    private var imageLast = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBinding = SearchFragmentBinding.inflate(layoutInflater)
        setTextListener()
        setScrollListener()
        gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        linearLayoutManager = LinearLayoutManager(requireContext())
        searchBinding.recyclerviewMain.layoutManager = gridLayoutManager
        searchBinding.recyclerviewSearch.layoutManager = linearLayoutManager
        searchBinding.recyclerviewSearch.setHasFixedSize(true)
        privateSearchItem = arrayListOf()
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        observeViewModel()

        searchBinding.upWardBtn.setOnClickListener {
            searchBinding.recyclerviewMain.scrollToPosition(0)
        }
        getCurrentSearch()
        return searchBinding.root
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

    private fun setScrollListener() = with(searchBinding){
        recyclerviewMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val visibleThreshold = 5
            private var previousTotalItemCount = 0
            private var currentPage = 0
            private var startingPageIndex = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var lastVisibleItemPosition = 0
                val layoutManager = recyclerviewMain.layoutManager
                val totalItemCount = layoutManager!!.itemCount


                val lastVisibleItems = (layoutManager as StaggeredGridLayoutManager)
                    .findLastCompletelyVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItems)

                if (totalItemCount < previousTotalItemCount) {
                    this.currentPage = this.startingPageIndex
                    this.previousTotalItemCount = totalItemCount
                }

                if (circularProgressBar.isVisible && (totalItemCount > previousTotalItemCount)) {
                    previousTotalItemCount = totalItemCount
                }

                if (!circularProgressBar.isVisible && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    queryImages(viewModel.queryText)
                }
            }
        })
    }

    private fun refreshStatus() {
        privateSearchItem.clear()
        viewModel.page = 1
        imageLast = false
        videoLast = false
    }

    private fun setTextListener() = with(searchBinding) {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    if (query.length >= 2) {
                        refreshStatus()
                        queryText = query
                        queryImages(query)
                        viewModel.insertSearchText(query)
                        searchBinding.root.hideKeyboard()
                        true
                    } else {
                        Toast.makeText(requireContext(), "두글자 이상 입력해주시기 바랍니다.", Toast.LENGTH_LONG)
                            .show()
                        false
                    }
                } else {
                    Toast.makeText(requireContext(), "query가 비어있습니다.", Toast.LENGTH_LONG).show()
                    false
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun queryImages(query: String) {
        if (!imageLast) {
            viewModel.getImages(query)
        }
        if (!videoLast) {
            viewModel.getVideos(query)
        }
        viewModel.page++
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated() {
        Log.i("tag", "reached the State.Created")
        getCurrentSearch()
        refreshStatus()
        gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        linearLayoutManager = LinearLayoutManager(requireContext())
        searchBinding.recyclerviewMain.layoutManager = gridLayoutManager
        searchBinding.recyclerviewSearch.layoutManager = linearLayoutManager
        searchBinding.recyclerviewSearch.setHasFixedSize(true)
        privateSearchItem = arrayListOf()
        searchAdapter = SearchAdapter(viewModel, privateSearchItem)
        searchBinding.recyclerviewMain.adapter = searchAdapter
        searchBinding.searchBar.setQuery("", false)
    }

    private fun getCurrentSearch() {
        viewModel.getSearchData()
    }

    private fun insertImageToMyPage(imageData: ImageData) {
        viewModel.insertImageToMyPage(imageData = imageData)
        Toast.makeText(requireContext(), "${imageData.title} 내 보관함에 추가 되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        observe(viewModel.searchListLiveData, ::handleImageList)
        observeToast(viewModel.showToast)
        observe(viewModel.searchLiveData, ::handleSearchData)
        observe(viewModel.deleteQuery, ::handleDeleteQuery)
        observeEvent(viewModel.clickImage, ::popupItemDetail)
    }

    private fun popupItemDetail(clickEvent: SingleEvent<SearchItem>) {
        clickEvent.getContentIfNotHandled()?.let {
            val searchItem = clickEvent.peekContent()
            val popupSearchClick = DialogPopup.newInstance(searchItem, false, requireContext())
            popupSearchClick.addChoiceListener(this)
            parentFragmentManager.beginTransaction().add(popupSearchClick, "DialogFragmnet Tag")
                .commitAllowingStateLoss()
        }
    }


    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        searchBinding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun bindImageListData(searchItems: SearchItems) = with(searchBinding) {
        if (!(searchItems.searchItemList.isNullOrEmpty())) {
            privateSearchItem.addAll(searchItems.searchItemList)
            privateSearchItem.sortDateTime()
            recyclerviewMain.adapter!!.notifyDataSetChanged()
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun bindLastImageData(searchItems: SearchItems) = with(searchBinding) {
        if (!(searchItems.searchItemList.isNullOrEmpty())) {
            if (searchItems.searchItemList[0].searchType == VIDEO_TYPE) {
                videoLast = true
            } else {
                imageLast = true
            }
            privateSearchItem.addAll(searchItems.searchItemList)
            privateSearchItem.sortDateTime()
            recyclerviewMain.adapter!!.notifyDataSetChanged()
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun handleSearchData(status: Resource<List<SearchData>>) {
        when (status) {
            is Resource.Loading -> showKeyLoadingView()
            is Resource.Success -> status.data?.let {
                bindSearchKeyData(it)
            }
            is Resource.LastSuccess -> status.data?.let {
                bindSearchKeyData(it)
            }
            is Resource.DataError -> {
                showSearchKey(false)
            }
        }
    }

    private fun handleDeleteQuery(status: Resource<Boolean>) {
        when (status) {
            is Resource.Loading -> showKeyLoadingView()
            is Resource.Success -> status.data?.let {
               if (it) {
                   getCurrentSearch()
               }
            }
            is Resource.LastSuccess -> status.data?.let {
                if (it) {
                    getCurrentSearch()
                }
            }
            is Resource.DataError -> {
                showSearchKey(false)
            }
        }
    }


    private fun bindSearchKeyData(it: List<SearchData>)  = with(searchBinding) {
        if (!(it.isNullOrEmpty())) {
            searchKeyAdapter = SearchKeyAdapter(viewModel, it)
            recyclerviewSearch.adapter = searchKeyAdapter
            recyclerviewSearch.adapter!!.notifyDataSetChanged()
            showSearchKey(true)
        } else {
            searchKeyAdapter = SearchKeyAdapter(viewModel, it)
            recyclerviewSearch.adapter = searchKeyAdapter
            recyclerviewSearch.adapter!!.notifyDataSetChanged()
            showSearchKey(false)
        }
    }


    private fun handleImageList(status: Resource<SearchItems>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let {
                bindImageListData(it)
            }
            is Resource.LastSuccess -> status.data?.let {
                bindLastImageData(it)
            }
            is Resource.DataError -> {
                if (status.errorCode == SEARCH_ERROR) {
                    returnErrorCode(status.errorCode).let { viewModel.showToastMessage(it) }
                    searchBinding.circularProgressBar.toGone()
                } else {
                    showDataView(false)
                    returnErrorCode(status.errorCode).let { viewModel.showToastMessage(it) }
                }
            }
        }
    }

    private fun showLoadingView() = with(searchBinding) {
        circularProgressBar.toVisible()
        tvNoData.toGone()
        recyclerviewSearch.toGone()
        searchHistoryTv.toGone()
        tvNoKeyData.toGone()
    }

    private fun showKeyLoadingView() = with(searchBinding) {
        circularProgressBar.toVisible()
        tvNoData.toGone()
        recyclerviewSearch.toVisible()
        searchHistoryTv.toVisible()
        recyclerviewMain.toGone()
        upWardBtn.toGone()
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

    private fun showSearchKey(show: Boolean) = with(searchBinding) {
        tvNoKeyData.visibility = if (show) GONE else VISIBLE
        recyclerviewSearch.visibility = if (show) VISIBLE else GONE
        searchHistoryTv.toVisible()
        circularProgressBar.toGone()
    }

    private fun showDataView(show: Boolean) = with(searchBinding) {
        tvNoData.visibility = if (show) GONE else VISIBLE
        recyclerviewMain.visibility = if (show) VISIBLE else GONE
        upWardBtn.visibility = if (show) VISIBLE else GONE
        circularProgressBar.toGone()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }

    override fun clickCancel() {

    }

    override fun clickOk(imageData: ImageData) {
        insertImageToMyPage(imageData)
    }


}