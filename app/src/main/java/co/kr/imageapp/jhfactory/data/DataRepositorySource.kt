package co.kr.imageapp.jhfactory.data

import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.data.dto.search.SearchData
import co.kr.imageapp.jhfactory.data.dto.search.SearchItems
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestImages(query: String, page: Int): Flow<Resource<SearchItems>>
    suspend fun requestVideos(query: String, page: Int): Flow<Resource<SearchItems>>
    fun requestInsertQuery(query: String): Flow<Resource<Boolean>>
    fun requestSearchDatas(): Flow<Resource<List<SearchData>>>
    fun deleteSearchData(searchKey: String): Flow<Resource<Boolean>>
    fun insertImageToMyPage(imageData: ImageData): Flow<Resource<Boolean>>
    fun selectImageToMyPage(): Flow<Resource<List<ImageData>>>
    fun deleteMyImage(imageData: ImageData): Flow<Resource<Boolean>>
}