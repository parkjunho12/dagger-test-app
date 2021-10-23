package co.kr.imageapp.kakao.ui.main.fragment.mypage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.kakao.const.KeyConst
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.CardviewItemImageBinding
import co.kr.imageapp.kakao.ui.base.listener.RecyclerItemListener
import co.kr.imageapp.kakao.util.loadImage
import co.kr.imageapp.kakao.util.loadVideo
import co.kr.imageapp.kakao.util.toGone
import co.kr.imageapp.kakao.util.toVisible

class MyPageViewHolder (private val itemBinding: CardviewItemImageBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(imageData: ImageData, recyclerItemListener: RecyclerItemListener) {
        itemBinding.img.loadVideo(imageData.imageUri)
        itemBinding.imgTitle.text = if (imageData.title.length >= 18) {
            "${imageData.title.substring(0, 18)}..."
        } else {
            imageData.title
        }
        itemBinding.deleteBtn.toVisible()
        itemBinding.imgDateTime.text = imageData.datetime.split("T")[0]
        itemBinding.deleteBtn.setOnClickListener {  }
        itemBinding.root.setOnClickListener {  }
    }
}