package co.kr.imageapp.kakao.ui.dialog

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import co.kr.imageapp.kakao.const.KeyConst.VIDEO_TYPE
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.databinding.DialogChoicePopupBinding
import co.kr.imageapp.kakao.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


private const val IMAGE_PARAM = "image_param"
private const val MYPAGE_PARAM ="my_param"

class DialogPopup(context: Context): DialogFragment() {
    private var onChoiceListener: OnChoiceListener? = null
    private var searchItem: SearchItem? = null
    val mContext = context
    private var isMypage = false
    private lateinit var choiceBinding: DialogChoicePopupBinding
    private var isShare = false
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Do if the permission is grante
                if (isShare) {
                    shareImages()
                } else {
                    downLoadImages()
                }
        }
        else {
            // Do otherwise
            Toast.makeText(requireContext(), "다운로드를 하려면 저장권한이 필요합니다.",Toast.LENGTH_SHORT).show()
        }
    }


    interface OnChoiceListener{
        fun clickCancel()
        fun clickOk(imageData: ImageData)
    }

    fun addChoiceListener(choiceListener: OnChoiceListener) {
        onChoiceListener = choiceListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchItem = it.getSerializable(IMAGE_PARAM) as SearchItem
            isMypage = it.getBoolean(MYPAGE_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        choiceBinding = DialogChoicePopupBinding.inflate(layoutInflater)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        setImageUrl()
        setTextView()
        setClickEvent()
        return choiceBinding.root
    }

    private fun shareImages() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        GlobalScope.launch(Dispatchers.IO) {
            val imageUri = if (searchItem!!.searchType == VIDEO_TYPE) {
                searchItem!!.thumbnail_url
            } else {
                searchItem!!.image_url
            }
            val bitmap = BitmapFactory.decodeStream(URL(imageUri).openStream())
            sharingIntent.setType("image/jpg")
            val fileManager = FileManager(requireContext())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sharingIntent.putExtra(Intent.EXTRA_STREAM,  fileManager.saveImageInQ(bitmap))
            } else {
                sharingIntent.putExtra(Intent.EXTRA_STREAM,  fileManager.getImageUri(requireContext(),bitmap))
            }
            withContext(Dispatchers.Main) {
                startActivity(Intent.createChooser(sharingIntent,"이미지 공유하기"))
            }
        }
    }

    private fun downLoadImages() {
        GlobalScope.launch(Dispatchers.IO) {
            val imageUri = if (searchItem!!.searchType == VIDEO_TYPE) {
                searchItem!!.thumbnail_url
            } else {
                searchItem!!.image_url
            }
            val bitmap = BitmapFactory.decodeStream(URL(imageUri).openStream())
            val fileManager = FileManager(requireContext())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                fileManager.saveImageInQ(bitmap)
            } else {
                fileManager.getImageUri(requireContext(),bitmap)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "${searchItem!!.title} 다운로드 되었습니다. 사진첩에서 확인해보세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setClickEvent() = with(choiceBinding) {
        if (searchItem != null) {
            downPopupBtn.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    isShare = false
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    downLoadImages()
                }
            }
            sharePopupBtn.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    isShare = true
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    shareImages()
                }
            }
            openBrowserBtn.setOnClickListener {
                if (searchItem != null) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            searchItem!!.url
                        )
                    )
                    startActivity(browserIntent)
                }
            }
        }
        popupOkBtn.setOnClickListener {
            if (searchItem != null) {
                val now = System.currentTimeMillis()
                val date = Date(now)
                val sdf = SimpleDateFormat("yyyy년 MM월 dd일,hh:mm:ss")
                val regDT =
//                    "2021년 10월 21일,22:12:23"
                    sdf.format(date)
                val imageData = if (searchItem!!.searchType == VIDEO_TYPE) {
                    ImageData(imageUri = searchItem!!.thumbnail_url,title = searchItem!!.title, linkUrl = searchItem!!.url,
                        playTime = searchItem!!.play_time!!, author = searchItem!!.author!!, datetime = searchItem!!.datetime, regDT = regDT)
                } else {
                    ImageData(imageUri = searchItem!!.image_url!!,title = searchItem!!.title, width = searchItem!!.width, height = searchItem!!.height,
                        linkUrl = searchItem!!.url, author = searchItem!!.author!!, datetime = searchItem!!.datetime, regDT = regDT)
                }
                onChoiceListener!!.clickOk(imageData)
            }
            dismiss()
        }
        popupCancelBtn.setOnClickListener {
            onChoiceListener!!.clickCancel()
            dismiss()
        }
    }

    private fun setTextView() = with(choiceBinding) {
        if (searchItem != null) {
            choicePopupTitle.text = searchItem!!.title
            dateTimePopup.text = searchItem!!.datetime.split("T")[0]
            if (searchItem!!.author == "") {
                authorPopup.toGone()
            } else {
                authorPopup.toVisible()
                authorPopup.text = searchItem!!.author
            }
        }
        if (isMypage) {
            openBrowserBtn.toGone()
            choicePopupContent.text = "관련 링크로 이동하시겠습니까?"
        } else {
            openBrowserBtn.toVisible()
            choicePopupContent.text = "내 보관함에 추가 하시겠습니까?"
        }
    }

    private fun setImageUrl() = with(choiceBinding){
        if (searchItem != null) {
            if (searchItem!!.searchType == VIDEO_TYPE) {
                imagePopup.loadVideo(searchItem!!.thumbnail_url)
                val secTime = searchItem!!.play_time!!%60
                val secStr = if (secTime < 10) {
                    "0$secTime"
                } else {
                    secTime
                }
                videoTimePopup.text = "${(searchItem!!.play_time!!.toInt() /60)}:$secStr"
                videoTimePopup.toVisible()
            } else {
                imagePopup.loadPopup(searchItem!!.image_url!!, searchItem!!.width, searchItem!!.height)
                videoTimePopup.toGone()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(
            searchItem: SearchItem,
            isMyPage: Boolean,
            context: Context
        ) =
            DialogPopup(context).apply {
                arguments = Bundle().apply {
                    putSerializable(IMAGE_PARAM, searchItem)
                    putBoolean(MYPAGE_PARAM, isMyPage)
                }
            }
    }
}