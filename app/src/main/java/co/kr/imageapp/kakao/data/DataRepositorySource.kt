package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.search.ImageList
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<ImageList>>
}