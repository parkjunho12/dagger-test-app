package co.kr.imageapp.kakao.ui.main.fragment.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.imageapp.kakao.data.DataRepositorySource
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.dto.search.VideoList
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

    fun getImages(query: String) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestImages(query).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun getVideos(query: String) {
        viewModelScope.launch {
            searchListLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestVideos(query).collect {
                searchListLiveDataPrivate.value = it
            }
        }
    }

    fun showToastMessage(errorMsg: String) {
        showToastPrivate.value = errorMsg
    }

}