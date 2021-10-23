package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(private val remoteRepository: RemoteData,
                                         private val ioDispatcher: CoroutineContext) : DataRepositorySource {

    override suspend fun requestImages(query: String, page: Int): Flow<Resource<SearchItems>> {
        return flow {
            emit(remoteRepository.requestImages(query, page))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestVideos(query: String, page: Int): Flow<Resource<SearchItems>> {
        return flow {
            emit(remoteRepository.requestVideos(query, page))
        }.flowOn(ioDispatcher)
    }
}