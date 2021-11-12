package co.kr.imageapp.jhfactory.data.remote

import co.kr.imageapp.jhfactory.data.Resource
import co.kr.imageapp.jhfactory.data.dto.search.SearchItems

internal interface RemoteDataSource {
    suspend fun requestImages(query: String, page: Int): Resource<SearchItems>
    suspend fun requestVideos(query: String, page: Int): Resource<SearchItems>
}