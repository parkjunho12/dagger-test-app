package co.kr.imageapp.kakao.data.dto.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Search")
data class SearchData(
    @PrimaryKey(autoGenerate = true) val index: Int = 0,
    @ColumnInfo val searchKey: String
) : Serializable