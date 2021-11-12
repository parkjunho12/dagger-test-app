package co.kr.imageapp.jhfactory.ui.main.fragment.mypage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.databinding.CardviewItemImageBinding
import co.kr.imageapp.jhfactory.ui.base.listener.MyPageRecyclerItemListener
import co.kr.imageapp.jhfactory.util.loadImage
import co.kr.imageapp.jhfactory.util.loadVideo
import co.kr.imageapp.jhfactory.util.toGone
import co.kr.imageapp.jhfactory.util.toVisible

class MyPageViewHolder (private val itemBinding: CardviewItemImageBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(imageData: ImageData, recyclerItemListener: MyPageRecyclerItemListener) {
        if (imageData.playTime == 0) {
            itemBinding.img.loadImage(imageData.imageUri, imageData.width, imageData.height)
        } else {
            itemBinding.img.layoutParams = ViewGroup.LayoutParams(500, 350)
            itemBinding.img.loadVideo(imageData.imageUri)
        }
        itemBinding.imgTitle.text = imageData.title
        itemBinding.deleteBtn.toVisible()
        itemBinding.imgDateTime.text = imageData.datetime.split("T")[0]
        if (imageData.playTime == 0) {
            itemBinding.videoTime.toGone()
        } else {
            itemBinding.videoTime.toVisible()
            val secTime = imageData.playTime %60
            val secStr = if (secTime < 10) {
                "0$secTime"
            } else {
                secTime
            }
            itemBinding.videoTime.text = "${(imageData.playTime.toInt() /60)}:$secStr"
        }
        if (imageData.author == "") {
            itemBinding.imgSiteName.toGone()
        }
        itemBinding.imgSiteName.text = imageData.author
        itemBinding.deleteBtn.setOnClickListener { recyclerItemListener.onDeleteClicked(imageData) }
        itemBinding.root.setOnClickListener { recyclerItemListener.onItemSelected(imageData) }
    }
}