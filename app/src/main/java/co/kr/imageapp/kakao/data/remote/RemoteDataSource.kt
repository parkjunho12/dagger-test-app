package co.kr.imageapp.kakao.data.remote

import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.ImageList

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<ImageList>
}