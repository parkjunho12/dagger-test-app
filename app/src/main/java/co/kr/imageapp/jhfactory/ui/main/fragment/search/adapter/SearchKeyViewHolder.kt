package co.kr.imageapp.jhfactory.ui.main.fragment.search.adapter

import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.jhfactory.data.dto.search.SearchData
import co.kr.imageapp.jhfactory.databinding.ItemSearchBinding
import co.kr.imageapp.jhfactory.ui.base.listener.RecyclerItemListener

class SearchKeyViewHolder(private val itemBinding: ItemSearchBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(searchItem: SearchData, recyclerItemListener: RecyclerItemListener) {
        itemBinding.searchTitle.text = searchItem.searchKey
        itemBinding.searchCancel.setOnClickListener{
            recyclerItemListener.onSearchKeySelected(searchData = searchItem)
        }
        itemBinding.searchConlayout.setOnClickListener {
            recyclerItemListener.onSearchKeyClick(searchData = searchItem)
        }
    }
}