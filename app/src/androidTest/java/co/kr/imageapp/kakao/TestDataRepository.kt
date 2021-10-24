package co.kr.imageapp.kakao

import co.kr.imageapp.kakao.DataStatus
import co.kr.imageapp.kakao.TestUtil.dataStatus
import co.kr.imageapp.kakao.TestUtil.initData
import co.kr.imageapp.kakao.data.DataRepositorySource
import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import co.kr.imageapp.kakao.data.error.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataRepository @Inject constructor() : DataRepositorySource {
    override suspend fun requestImages(query: String, page: Int): Flow<Resource<SearchItems>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<SearchItems>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(SearchItems(arrayListOf()))) }
            }
        }
    }

    override suspend fun requestVideos(query: String, page: Int): Flow<Resource<SearchItems>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<SearchItems>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(SearchItems(arrayListOf()))) }
            }
        }
    }

    override fun requestInsertQuery(query: String): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override fun requestSearchDatas(): Flow<Resource<List<SearchData>>> {
        return flow { emit(Resource.Success(listOf(SearchData("공유"), SearchData("김고은")))) }
    }

    override fun deleteSearchData(searchKey: String): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override fun insertImageToMyPage(imageData: ImageData): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override fun selectImageToMyPage(): Flow<Resource<List<ImageData>>> {
        return flow { emit(Resource.Success(listOf(ImageData("https://t1.daumcdn.net/news/201808/20/seoul/20180820220101977riwp.jpg",
        title = "뉴스 이미지", linkUrl = "http://v.media.daum.net/v/20180820220101407",
            author = "서울신문", datetime = "2018-08-20T22:01:01.000+09:00", regDT = "2021년 10월 24일")))) }
    }

    override fun deleteMyImage(imageData: ImageData): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }
}