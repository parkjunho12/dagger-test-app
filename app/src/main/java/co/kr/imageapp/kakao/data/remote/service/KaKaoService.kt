package co.kr.imageapp.kakao.data.remote.service

import co.kr.imageapp.kakao.const.KeyConst
import co.kr.imageapp.kakao.data.dto.search.ImageContent
import co.kr.imageapp.kakao.data.dto.search.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface KaKaoService {
    @Headers("Authorization: KakaoAK ${KeyConst.REST_API_KEY}")
    @GET("v2/search/image")
    suspend fun fetchImages(): Response<ImageList>
}