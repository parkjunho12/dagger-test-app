package co.kr.imageapp.jhfactory.data.remote.service

import co.kr.imageapp.jhfactory.const.KeyConst
import co.kr.imageapp.jhfactory.data.dto.search.ImageList
import co.kr.imageapp.jhfactory.data.dto.search.VideoList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KaKaoService {
    @Headers("Authorization: KakaoAK ${KeyConst.REST_API_KEY}")
    @GET("v2/search/image")
    suspend fun fetchImages(@Query("query") query: String,@Query("page") page: Int = 1): Response<ImageList>


    @Headers("Authorization: KakaoAK ${KeyConst.REST_API_KEY}")
    @GET("v2/search/vclip")
    suspend fun fetchVideos(@Query("query") query: String,@Query("page") page: Int = 1): Response<VideoList>
}