package co.kr.imageapp.kakao.ui.main.fragment.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.const.KeyConst.VIDEO_TYPE
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.kakao.util.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class SearchViewHolder(private val itemBinding: CardviewItemImageBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(searchItem: SearchItem, recyclerItemListener: RecyclerItemListener) {
        if (searchItem.searchType == VIDEO_TYPE) {
            itemBinding.img.layoutParams = ViewGroup.LayoutParams(500, 350)
            itemBinding.img.loadVideo(searchItem.thumbnail_url)
            val secTime = searchItem.play_time!!%60
            val secStr = if (secTime < 10) {
                "0$secTime"
            } else {
                secTime
            }
            itemBinding.videoTime.text = "${(searchItem.play_time!!.toInt() /60)}:$secStr"
            itemBinding.videoTime.toVisible()
        } else {
            if (searchItem.image_url == null) {
                itemBinding.img.loadImage(searchItem.thumbnail_url, searchItem.width, searchItem.height)
            } else {
                itemBinding.img.loadImage(searchItem.image_url, searchItem.width, searchItem.height)
            }
            itemBinding.videoTime.toGone()
        }
        itemBinding.deleteBtn.toGone()
        itemBinding.imgTitle.text = if (searchItem.title.length >= 18) {
            "${searchItem.title.substring(0, 18)}..."
        } else {
            searchItem.title
        }
        if (searchItem.author == "") {
            itemBinding.imgSiteName.toGone()
        } else {
            itemBinding.imgSiteName.toVisible()
            itemBinding.imgSiteName.text = searchItem.author
        }
        itemBinding.imgDateTime.text = searchItem.datetime.split("T")[0]
        itemBinding.root.setOnClickListener {
            recyclerItemListener.onItemSelected(searchItem)
        }
    }
}