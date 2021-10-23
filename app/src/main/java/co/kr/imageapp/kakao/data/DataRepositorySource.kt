package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.dto.search.VideoList
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestImages(query: String): Flow<Resource<SearchItems>>
    suspend fun requestVideos(query: String): Flow<Resource<SearchItems>>
}