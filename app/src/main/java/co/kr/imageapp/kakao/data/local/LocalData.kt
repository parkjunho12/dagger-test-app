package co.kr.imageapp.kakao.data.local

import co.kr.imageapp.kakao.data.Resource
import co.kr.imageapp.kakao.data.dto.search.SearchData
import co.kr.imageapp.kakao.data.error.INSERT_ERROR
import co.kr.imageapp.kakao.data.error.SELECT_ERROR
import co.kr.imageapp.kakao.data.local.mypage.ImageDao
import co.kr.imageapp.kakao.data.local.search.SearchDao
import java.lang.Exception
import javax.inject.Inject

class LocalData @Inject
constructor(private val imageDao: ImageDao, private val searchDao: SearchDao): LocalDataSource {

    override fun insertSearchQuery(query: String): Resource<Boolean> {
        return try {
            searchDao.insertAll(SearchData(searchKey = query))
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.DataError(INSERT_ERROR)
        }
    }

    override fun requestSearchDatas(): Resource<List<SearchData>> {
        return try {
            Resource.Success(searchDao.getAll())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.DataError(SELECT_ERROR)
        }
    }

    override fun deleteSearchData(searchKey: String): Resource<Boolean> {
        return try {
            searchDao.delete(searchKey)
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.DataError(SELECT_ERROR)
        }
    }
}