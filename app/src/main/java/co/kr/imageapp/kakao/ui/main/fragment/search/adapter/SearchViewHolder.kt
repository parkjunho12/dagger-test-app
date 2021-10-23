package co.kr.imageapp.kakao.ui.main.fragment.search.adapter

import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class SearchViewHolder(private val itemBinding: CardviewItemImageBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(searchItem: SearchItem, recyclerItemListener: RecyclerItemListener) {
        Picasso.get().load(searchItem.thumbnail_url).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(itemBinding.img)
        itemBinding.imgTitle.text = searchItem.title
        itemBinding.imgSiteName.text = searchItem.author
        itemBinding.imgDateTime.text = searchItem.datetime.split("T")[0]
    }
}