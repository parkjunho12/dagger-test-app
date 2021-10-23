package co.kr.imageapp.kakao.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


private const val TITLE_PARAM = "title_param"
private const val CONTENT_PARAM = "content_param"
private const val CHOICE_PARAM = "choice_param"

class DialogPopup(context: Context): DialogFragment() {
    private var onChoiceListener: OnChoiceListener? = null
    private var mTitle: String? = null
    private var mContent: String? = null
    private var isChoice: Boolean? = null
    private var isClickOk: Boolean = false
    val mContext = context

    interface OnChoiceListener{
        fun clickCancel()
        fun clickOk()
    }

    fun addChoiceListener(choiceListener: OnChoiceListener) {
        onChoiceListener = choiceListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTitle = it.getString(TITLE_PARAM)
            mContent = it.getString(CONTENT_PARAM)
            isChoice = it.getBoolean(CHOICE_PARAM)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            content: String,
            isChoice: Boolean,
            context: Context
        ) =
            DialogPopup(context).apply {
                arguments = Bundle().apply {
                    putString(TITLE_PARAM, title)
                    putString(CONTENT_PARAM, content)
                    putBoolean(CHOICE_PARAM, isChoice)
                }
            }
    }
}