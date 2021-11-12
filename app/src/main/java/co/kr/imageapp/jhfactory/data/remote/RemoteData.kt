package co.kr.imageapp.jhfactory.data.remote

import co.kr.imageapp.jhfactory.const.KeyConst.IMAGE_TYPE
import co.kr.imageapp.jhfactory.const.KeyConst.VIDEO_TYPE
import co.kr.imageapp.jhfactory.data.Resource
import co.kr.imageapp.jhfactory.data.dto.search.ImageList
import co.kr.imageapp.jhfactory.data.dto.search.SearchItem
import co.kr.imageapp.jhfactory.data.dto.search.SearchItems
import co.kr.imageapp.jhfactory.data.dto.search.VideoList
import co.kr.imageapp.jhfactory.data.error.NETWORK_ERROR
import co.kr.imageapp.jhfactory.data.error.NO_INTERNET_CONNECTION
import co.kr.imageapp.jhfactory.data.error.SEARCH_ERROR
import co.kr.imageapp.jhfactory.data.remote.service.KaKaoService
import co.kr.imageapp.jhfactory.util.NetworkConnectivity
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) : RemoteDataSource {
    override suspend fun requestImages(query: String, page: Int): Resource<SearchItems> {
        val imageService = serviceGenerator.createService(KaKaoService::class.java)
        return when (val responseBody: Any = processCall(imageService.fetchImages(query, page)) ?: SEARCH_ERROR) {
            is ImageList -> {
                val searchItems: ArrayList<SearchItem> = arrayListOf()
                responseBody.documents.forEach { imageContent ->
                    searchItems.add(SearchItem(title = "$query - ${collectionToString(imageContent.collection)}", thumbnail_url = imageContent.thumbnail_url, image_url = imageContent.image_url,
                        url = imageContent.doc_url, play_time = null, datetime = imageContent.datetime,
                        author = imageContent.display_sitename, width = imageContent.width, height = imageContent.height, searchType = IMAGE_TYPE))
                }
                if ( responseBody.documents.isEmpty()) {
                    return Resource.DataError(errorCode = SEARCH_ERROR)
                }
                if (responseBody.meta.is_end) {
                    return Resource.LastSuccess(SearchItems(searchItems))
                }
                Resource.Success(data = SearchItems(searchItems))
            }
            else -> {
                Resource.DataError(errorCode = responseBody as Int)
            }
        }
    }

    override suspend fun requestVideos(query: String, page: Int): Resource<SearchItems> {
        val videoService = serviceGenerator.createService(KaKaoService::class.java)
        return when (val responseBody: Any = processCall(videoService.fetchVideos(query, page)) ?: SEARCH_ERROR) {
            is VideoList -> {
                val searchItems: ArrayList<SearchItem> = arrayListOf()
                responseBody.documents.forEach { videoContent ->
                    searchItems.add(SearchItem(title = videoContent.title, thumbnail_url = videoContent.thumbnail, image_url = null, url = videoContent.url,
                        play_time = videoContent.play_time, datetime = videoContent.datetime, author = videoContent.author, searchType = VIDEO_TYPE))
                }
                if ( responseBody.documents.isEmpty()) {
                    return Resource.DataError(errorCode = SEARCH_ERROR)
                }
                if (responseBody.meta.is_end) {
                    return Resource.LastSuccess(SearchItems(searchItems))
                }
                Resource.Success(data = SearchItems(searchItems))
            }
            else -> {
                Resource.DataError(errorCode = responseBody as Int)
            }
        }
    }

    private fun collectionToString(collection: String): String {
        return when(collection) {
            "news" -> "뉴스 이미지"
            "blog" -> "블로그 이미지"
            "etc" -> "기타 이미지"
            else -> collection
        }
    }

    private suspend fun processCall(response: Response<*>): Any? {
        return if (!networkConnectivity.isConnected()) {
            NO_INTERNET_CONNECTION
        } else {
            try {
                if (response.isSuccessful) {
                    response.body()
                } else {
                    response.code()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                NETWORK_ERROR
            }
        }
    }
}
