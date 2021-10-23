package co.kr.imageapp.kakao.ui.main.fragment.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.imageapp.kakao.data.DataRepositorySource
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val searchListLiveDataPrivate = MutableLiveData<Resource<SearchItems>>()
    val searchListLiveData: LiveData<Resource<SearchItems>> get() = searchListLiveDataPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val deleteQueryPrivate = MutableLiveData<Resource<Boolean>>()
    val deleteQuery: LiveData<Resource<Boolean>> get() = deleteQueryPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val searchLiveDataPrivate = MutableLiveData<Resource<List<SearchData>>>()
    val searchLiveData: LiveData<Resource<List<SearchData>>> get() = searchLiveDataPrivate


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val clickImagePrivate = MutableLiveData<SingleEvent<SearchItem>>()
    val clickImage: LiveData<SingleEvent<SearchItem>> get() = clickImagePrivate

    var page = 1
    var queryText = ""


    fun getImages(query: String) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            queryText = query
            dataRepositoryRepository.requestImages(queryText, page).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun getVideos(query: String) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            queryText = query
            dataRepositoryRepository.requestVideos(queryText, page).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun insertSearchText(query: String) {
        dataRepositoryRepository.requestInsertQuery(query)
    }

    fun getSearchData() {
        viewModelScope.launch {
            searchLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestSearchDatas().collect {
                searchLiveDataPrivate.value = it
            }
        }
    }

    fun deleteSearchData(searchKey: String) {
        viewModelScope.launch {
            deleteQueryPrivate.value = Resource.Loading()
            dataRepositoryRepository.deleteSearchData(searchKey).collect {
                deleteQueryPrivate.value = it
            }
        }
    }

    fun clickImage(searchItem: SearchItem) {
        clickImagePrivate.value = SingleEvent(searchItem)
    }

    fun showToastMessage(errorMsg: String) {
        showToastPrivate.value = SingleEvent(errorMsg)
    }

}