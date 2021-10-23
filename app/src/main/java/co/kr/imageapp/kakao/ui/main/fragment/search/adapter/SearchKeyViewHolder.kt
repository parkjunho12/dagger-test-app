package co.kr.imageapp.kakao.ui.main.fragment.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.const.KeyConst
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.databinding.ItemSearchBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.kakao.util.loadImage
import co.kr.imageapp.kakao.util.loadVideo
import co.kr.imageapp.kakao.util.toGone
import co.kr.imageapp.kakao.util.toVisible

class SearchKeyViewHolder(private val itemBinding: ItemSearchBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(searchItem: SearchData, recyclerItemListener: RecyclerItemListener) {
        itemBinding.searchTitle.text = searchItem.searchKey
        itemBinding.searchCancel.setOnClickListener{
            recyclerItemListener.onSearchKeySelected(searchData = searchItem)
        }
    }
}