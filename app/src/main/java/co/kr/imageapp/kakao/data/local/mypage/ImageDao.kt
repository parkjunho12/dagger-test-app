package co.kr.imageapp.kakao.data.local.mypage

import androidx.lifecycle.LiveData
import androidx.room.*
import co.kr.imageapp.kakao.data.dto.mypage.ImageData


@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg image: ImageData)

    @Query("SELECT * FROM Images")
    fun getAll(): List<ImageData>

    @Update
    fun update(image: ImageData)

    @Delete
    fun delete(image: ImageData)
}