package co.kr.imageapp.jhfactory.util

import co.kr.imageapp.jhfactory.data.dto.search.SearchItem
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

fun ArrayList<SearchItem>.sortDateTime() {
    Collections.sort(this,
        Comparator<SearchItem> { searchItem1, searchItem2 ->
            return@Comparator searchItem2.datetime.compareTo(searchItem1.datetime)
        })
}