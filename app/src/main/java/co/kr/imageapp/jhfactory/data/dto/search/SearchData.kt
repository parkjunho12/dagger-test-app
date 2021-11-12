package co.kr.imageapp.jhfactory.data.dto.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Search")
data class SearchData(
    @PrimaryKey
    @ColumnInfo
    val searchKey: String
) : Serializable