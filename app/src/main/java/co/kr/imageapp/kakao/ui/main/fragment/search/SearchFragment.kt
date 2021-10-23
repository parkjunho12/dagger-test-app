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
import co.kr.imageapp.kakao.const.KeyConst.VIDEO_TYPE
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.error.DEFAULT_ERROR
import co.kr.imageapp.kakao.data.error.NETWORK_ERROR
import co.kr.imageapp.kakao.data.error.NO_INTERNET_CONNECTION
import co.kr.imageapp.kakao.data.error.SEARCH_ERROR
import co.kr.imageapp.kakao.databinding.SearchFragmentBinding
import co.kr.imageapp.kakao.ui.main.fragment.search.adapter.SearchAdapter
import co.kr.imageapp.kakao.ui.main.fragment.search.adapter.SearchKeyAdapter
import co.kr.imageapp.kakao.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchBinding: SearchFragmentBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchKeyAdapter: SearchKeyAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
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
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        linearLayoutManager = LinearLayoutManager(requireContext())
        searchBinding.recyclerviewMain.layoutManager = gridLayoutManager
        searchBinding.recyclerviewSearch.layoutManager = linearLayoutManager
        privateSearchItem = arrayListOf()
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        observeViewModel()
        searchAdapter = SearchAdapter(viewModel, privateSearchItem)
        searchBinding.recyclerviewMain.adapter = searchAdapter
        searchBinding.upWardBtn.setOnClickListener {
            searchBinding.recyclerviewMain.scrollToPosition(0)
        }
        getCurrentSearch()
        return searchBinding.root
    }

    private fun setScrollListener() = with(searchBinding){
        recyclerviewMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerviewMain.layoutManager

                if (!circularProgressBar.isVisible) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 5개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + 5 && lastVisibleItem != -1) {
                        queryImages(viewModel.queryText)
                    }
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
        searchBinding.searchBar.setQuery("", false)
    }

    private fun getCurrentSearch() {
        viewModel.getSearchData()
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
        tvNoKeyData.toGone()
    }

    private fun showKeyLoadingView() = with(searchBinding) {
        circularProgressBar.toVisible()
        tvNoData.toGone()
        recyclerviewSearch.toVisible()
        recyclerviewMain.toGone()
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
        circularProgressBar.toGone()
    }

    private fun showDataView(show: Boolean) = with(searchBinding) {
        tvNoData.visibility = if (show) GONE else VISIBLE
        recyclerviewMain.visibility = if (show) VISIBLE else GONE
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


}