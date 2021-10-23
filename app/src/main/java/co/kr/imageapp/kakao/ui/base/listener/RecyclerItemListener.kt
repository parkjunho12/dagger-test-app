package co.kr.imageapp.kakao.ui.base.listener

import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem

interface RecyclerItemListener {
    fun onItemSelected(searchItem : SearchItem)
    fun onSearchKeySelected(searchData: SearchData)
}