package co.kr.imageapp.kakao.util

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

fun ArrayList<SearchItem>.sortDateTime() {
    Collections.sort(this,
        Comparator<SearchItem> { searchItem1, searchItem2 ->
            return@Comparator searchItem2.datetime.compareTo(searchItem1.datetime)
        })
}