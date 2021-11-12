package co.kr.imageapp.jhfactory.ui.main.fragment.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData
import co.kr.imageapp.jhfactory.databinding.CardviewItemImageBinding
import co.kr.imageapp.jhfactory.ui.base.listener.MyPageRecyclerItemListener
import co.kr.imageapp.jhfactory.ui.main.fragment.mypage.MyPageViewModel

class MyPageAdapter(private val myPageViewModel: MyPageViewModel, private val imageItemList: List<ImageData>):
    RecyclerView.Adapter<MyPageViewHolder>(){

    private val onItemClickListener: MyPageRecyclerItemListener = object : MyPageRecyclerItemListener {
        override fun onItemSelected(imageData: ImageData) {
            myPageViewModel.clickImage(imageData)
        }

        override fun onDeleteClicked(imageData: ImageData) {
            myPageViewModel.deleteImage(imageData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageViewHolder {
        val view = CardviewItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageItemList.size
    }

    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        holder.bind(imageItemList[position], onItemClickListener)
    }
}