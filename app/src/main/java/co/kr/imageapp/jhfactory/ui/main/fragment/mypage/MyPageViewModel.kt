package co.kr.imageapp.jhfactory.ui.main.fragment.mypage

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.imageapp.jhfactory.data.DataRepositorySource
import co.kr.imageapp.jhfactory.data.Resource
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MyPageViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val imageLiveDataPrivate = MutableLiveData<Resource<List<ImageData>>>()
    val imageLiveData: LiveData<Resource<List<ImageData>>> get() = imageLiveDataPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val clickMyImagePrivate = MutableLiveData<SingleEvent<ImageData>>()
    val clickMyImage: LiveData<SingleEvent<ImageData>> get() = clickMyImagePrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val deleteMyImagePrivate = MutableLiveData<Resource<Boolean>>()
    val deleteMyImage: LiveData<Resource<Boolean>> get() = deleteMyImagePrivate


    fun getMyImages() {
        viewModelScope.launch {
            imageLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.selectImageToMyPage().collect {
                imageLiveDataPrivate.value = it
            }
        }
    }

    fun showToastMessage(errorMsg: String) {
        showToastPrivate.value = SingleEvent(errorMsg)
    }

    fun clickImage(imageData: ImageData) {
        clickMyImagePrivate.value = SingleEvent(imageData)
    }

    fun deleteImage(imageData: ImageData) {
        viewModelScope.launch {
            imageLiveDataPrivate.value = Resource.Loading()
            dataRepositoryRepository.deleteMyImage(imageData).collect {
                deleteMyImagePrivate.value = it
            }
        }
    }


}