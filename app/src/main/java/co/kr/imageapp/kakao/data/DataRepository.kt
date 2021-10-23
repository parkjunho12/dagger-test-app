package co.kr.imageapp.kakao.data

import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.local.LocalData
import co.kr.imageapp.kakao.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(private val remoteRepository: RemoteData,
                                         private val localData: LocalData,
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

    override fun requestInsertQuery(query: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.insertSearchQuery(query))
        }.flowOn(ioDispatcher)
    }

    override fun requestSearchDatas(): Flow<Resource<List<SearchData>>> {
        return flow {
            emit(localData.requestSearchDatas())
        }.flowOn(ioDispatcher)
    }

    override fun deleteSearchData(searchKey: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.deleteSearchData(searchKey))
        }.flowOn(ioDispatcher)
    }

    override fun insertImageToMyPage(imageData: ImageData): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.insertImageToMyPage(imageData))
        }.flowOn(ioDispatcher)
    }

    override fun selectImageToMyPage(): Flow<Resource<List<ImageData>>> {
        return flow {
            emit(localData.selectImageToMyPage())
        }.flowOn(ioDispatcher)
    }

    override fun deleteMyImage(imageData: ImageData): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.deleteMyImage(imageData))
        }.flowOn(ioDispatcher)
    }
}