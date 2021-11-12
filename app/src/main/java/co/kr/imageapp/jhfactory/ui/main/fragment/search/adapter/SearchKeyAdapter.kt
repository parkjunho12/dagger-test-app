package co.kr.imageapp.jhfactory.ui.main.fragment.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.jhfactory.data.dto.search.SearchData
import co.kr.imageapp.jhfactory.data.dto.search.SearchItem
import co.kr.imageapp.jhfactory.databinding.ItemSearchBinding
import co.kr.imageapp.jhfactory.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.jhfactory.ui.main.fragment.search.SearchViewModel

class SearchKeyAdapter (private val searchViewModel: SearchViewModel, private val searchItemList: List<SearchData>):
    RecyclerView.Adapter<SearchKeyViewHolder>(){

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(searchItem: SearchItem) {

        }

        override fun onSearchKeySelected(searchData: SearchData) {
            searchViewModel.deleteSearchData(searchData.searchKey)
        }

        override fun onSearchKeyClick(searchData: SearchData) {
            searchViewModel.getImages(searchData.searchKey)
            searchViewModel.getVideos(searchData.searchKey)
            searchViewModel.page++
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