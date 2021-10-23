package co.kr.imageapp.kakao.data.remote

import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.dto.search.VideoList

internal interface RemoteDataSource {
    suspend fun requestImages(query: String, page: Int): Resource<SearchItems>
    suspend fun requestVideos(query: String, page: Int): Resource<SearchItems>
}