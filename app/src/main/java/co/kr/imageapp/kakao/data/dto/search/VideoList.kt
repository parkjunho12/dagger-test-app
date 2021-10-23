package co.kr.imageapp.kakao.data.dto.search

import java.io.Serializable

data class VideoList(
    val meta: MetaData,
    var documents: List<VideoContent>
) : Serializable