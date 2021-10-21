package co.kr.imageapp.kakao.data.dto.search

import java.io.Serializable

data class ImageList(
    val meta: MetaData,
    var documents: List<ImageContent>
) : Serializable