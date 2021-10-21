package co.kr.imageapp.kakao.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.imageapp.kakao.data.DataRepositorySource
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@HiltViewModel
class MainViewModel  @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val imageListLiveDataPrivate = MutableLiveData<Resource<ImageList>>()
    val imageListLiveData: LiveData<Resource<ImageList>> get() = imageListLiveDataPrivate

    fun getImages() {
        viewModelScope.launch {
            imageListLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.requestRecipes().collect {
                imageListLiveDataPrivate.value = it
            }
        }
    }
}