package co.kr.imageapp.kakao.data.dto.mypage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Images")
data class ImageData(
    @PrimaryKey
    @ColumnInfo
    val imageUri: String,
    @ColumnInfo val title: String,
    @ColumnInfo val linkUrl: String,
    @ColumnInfo val playTime: Int = 0,
    @ColumnInfo val author: String = "",
    @ColumnInfo val datetime: String
) : Serializable