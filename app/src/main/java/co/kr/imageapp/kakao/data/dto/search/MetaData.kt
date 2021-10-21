package co.kr.imageapp.kakao.data.dto.search

import java.io.Serializable

data class MetaData(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
): Serializable
