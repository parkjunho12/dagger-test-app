package co.kr.imageapp.kakao.ui.main.fragment.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.databinding.ItemSearchBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchViewModel

class SearchKeyAdapter (private val searchViewModel: SearchViewModel, private val searchItemList: List<SearchData>):
    RecyclerView.Adapter<SearchKeyViewHolder>(){

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(searchItem: SearchItem) {
            searchViewModel
        }

        override fun onSearchKeySelected(searchData: SearchData) {
            searchViewModel.deleteSearchData(searchData.searchKey)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeyViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchKeyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    override fun onBindViewHolder(holder: SearchKeyViewHolder, position: Int) {
        holder.bind(searchItemList[position], onItemClickListener)
    }
}