package co.kr.imageapp.jhfactory.data.local.search

import androidx.room.*
import co.kr.imageapp.jhfactory.data.dto.search.SearchData


@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg search: SearchData)

    @Query("SELECT * FROM Search")
    fun getAll(): List<SearchData>

    @Update
    fun update(searhData: SearchData)

    @Query("DELETE FROM Search WHERE searchKey = :searchKey")
    fun delete(searchKey: String)
}