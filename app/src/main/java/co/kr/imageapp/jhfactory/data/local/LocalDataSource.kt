package co.kr.imageapp.jhfactory.data.local

import co.kr.imageapp.jhfactory.data.Resource
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.data.dto.search.SearchData


interface LocalDataSource {
    fun insertSearchQuery(query: String): Resource<Boolean>
    fun requestSearchDatas(): Resource<List<SearchData>>
    fun deleteSearchData(searchKey: String): Resource<Boolean>
    fun insertImageToMyPage(imageData: ImageData): Resource<Boolean>
    fun selectImageToMyPage(): Resource<List<ImageData>>
    fun deleteMyImage(imageData: ImageData): Resource<Boolean>
}