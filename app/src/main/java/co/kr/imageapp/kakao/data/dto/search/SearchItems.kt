package co.kr.imageapp.kakao.data.dto.search


data class SearchItems(val searchItemList: ArrayList<SearchItem>)

data class SearchItem(
    val title: String,
    val thumbnail_url: String,
    val image_url: String?,
    val url: String,
    val play_time: Int?,
    val datetime: String,
    val author: String?,
    val width: Int = 0,
    val height: Int = 0,
    val searchType: Int
)