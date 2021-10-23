package co.kr.imageapp.kakao.ui.base.listener

import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem

interface RecyclerItemListener {
    fun onItemSelected(searchItem : SearchItem)
    fun onSearchKeySelected(searchData: SearchData)
    fun onSearchKeyClick(searchData: SearchData)
}

interface MyPageRecyclerItemListener {
    fun onItemSelected(imageData: ImageData)
    fun onDeleteClicked(imageData: ImageData)
}