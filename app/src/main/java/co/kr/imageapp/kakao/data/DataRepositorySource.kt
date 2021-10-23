package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.dto.search.VideoList
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestImages(query: String, page: Int): Flow<Resource<SearchItems>>
    suspend fun requestVideos(query: String, page: Int): Flow<Resource<SearchItems>>
    fun requestInsertQuery(query: String): Flow<Resource<Boolean>>
    fun requestSearchDatas(): Flow<Resource<List<SearchData>>>
    fun deleteSearchData(searchKey: String): Flow<Resource<Boolean>>
}