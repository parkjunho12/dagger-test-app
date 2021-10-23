package co.kr.imageapp.kakao.ui.main.fragment.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchViewModel

class SearchAdapter(private val searchViewModel: SearchViewModel, private val searchItemList: ArrayList<SearchItem>):
        RecyclerView.Adapter<SearchViewHolder>(){

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(searchItem: SearchItem) {
            searchViewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = CardviewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchItemList[position], onItemClickListener)
    }
}



