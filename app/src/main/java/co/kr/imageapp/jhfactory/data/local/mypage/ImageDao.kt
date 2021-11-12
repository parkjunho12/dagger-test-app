package co.kr.imageapp.jhfactory.data.local.mypage

import androidx.room.*
import co.kr.imageapp.jhfactory.data.dto.mypage.ImageData


@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg image: ImageData)

    @Query("SELECT * FROM Images ORDER BY regDT ASC")
    fun getAll(): List<ImageData>

    @Update
    fun update(image: ImageData)

    @Delete
    fun delete(image: ImageData)
}