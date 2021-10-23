package co.kr.imageapp.kakao.data.local

import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import kotlinx.coroutines.flow.Flow


interface LocalDataSource {
    fun insertSearchQuery(query: String): Resource<Boolean>
    fun requestSearchDatas(): Resource<List<SearchData>>
    fun deleteSearchData(searchKey: String): Resource<Boolean>
    fun insertImageToMyPage(imageData: ImageData): Resource<Boolean>
    fun selectImageToMyPage(): Resource<List<ImageData>>
}