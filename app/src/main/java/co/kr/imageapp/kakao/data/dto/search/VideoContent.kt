package co.kr.imageapp.kakao.data.dto.search

import java.io.Serializable

data class VideoContent(
    val title: String,
    val url: String,
    val datetime: String,
    val play_time: Int,
    val thumbnail: String,
    val author: String
) : Serializable {
    companion object {
        val EMPTY_VIDEO = VideoContent("", "", "", 0,"", "")
    }
}