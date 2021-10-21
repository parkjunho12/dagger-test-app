package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.search.ImageList
import co.kr.imageapp.kakao.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(private val remoteRepository: RemoteData,
                                         private val ioDispatcher: CoroutineContext) : DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<ImageList>> {
        return flow {
            emit(remoteRepository.requestRecipes())
        }.flowOn(ioDispatcher)
    }
}