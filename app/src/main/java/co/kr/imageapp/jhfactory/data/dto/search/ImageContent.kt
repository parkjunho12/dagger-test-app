package co.kr.imageapp.jhfactory.data.dto.search

import java.io.Serializable

data class ImageContent(
    val collection: String,
    val thumbnail_url: String,
    val image_url: String,
    val width: Int,
    val height: Int,
    val display_sitename: String,
    val doc_url: String,
    val datetime: String
) : Serializable {
    companion object {
        val EMPTY_IMAGE = ImageContent("", "", "", 0, 0, "", "", "")
    }
}