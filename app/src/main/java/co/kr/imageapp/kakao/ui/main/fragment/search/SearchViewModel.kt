package co.kr.imageapp.kakao.ui.main.fragment.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.imageapp.kakao.data.DataRepositorySource
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItems
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
    private val showToastPrivate = MutableLiveData<String>()
    val showToast: LiveData<String> get() = showToastPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val insertQueryPrivate = MutableLiveData<Resource<Boolean>>()
    val insertQuery: LiveData<Resource<Boolean>> get() = insertQueryPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val deleteQueryPrivate = MutableLiveData<Resource<Boolean>>()
    val deleteQuery: LiveData<Resource<Boolean>> get() = deleteQueryPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val searchLiveDataPrivate = MutableLiveData<Resource<List<SearchData>>>()
    val searchLiveData: LiveData<Resource<List<SearchData>>> get() = searchLiveDataPrivate


    fun getImages(query: String, page: Int) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestImages(query, page).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun getVideos(query: String, page: Int) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestVideos(query, page).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun insertSearchText(query: String) {
        viewModelScope.launch {
            insertQueryPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestInsertQuery(query).collect {
                insertQueryPrivate.value = it
            }
        }
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

    fun showToastMessage(errorMsg: String) {
        showToastPrivate.value = errorMsg
    }

}