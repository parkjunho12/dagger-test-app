package co.kr.imageapp.kakao

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import co.kr.imageapp.kakao.data.dto.search.SearchItem
import co.kr.imageapp.kakao.data.dto.search.SearchItems
import com.google.gson.Gson
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.lang.reflect.Type

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var searchItems: SearchItems = SearchItems(arrayListOf())

    fun initData(): SearchItems {
        val gson = Gson()
        val type: Type = TypeToken.getParameterized(List::class.java, SearchItem::class.java).type
        val jsonString = getJson("SearchApiResponse.json")
        searchItems = gson.fromJson<SearchItems>(jsonString, type)
        return searchItems

        return SearchItems(arrayListOf())
    }


    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}