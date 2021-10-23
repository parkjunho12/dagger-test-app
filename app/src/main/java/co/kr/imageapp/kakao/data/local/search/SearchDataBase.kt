package co.kr.imageapp.kakao.data.local.search

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import co.kr.imageapp.kakao.data.dto.search.SearchData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SearchData::class], version = 3)
abstract class SearchDataBase : RoomDatabase(){
    abstract fun searchDao(): SearchDao

    companion object {
        private const val SEARCH_DB_NAME = "search_db"

        fun getInstance(context: Context) : SearchDataBase {
            return buildDatabase(context)
        }

        private fun buildDatabase(context: Context): SearchDataBase {
            return Room.databaseBuilder(context, SearchDataBase::class.java, SEARCH_DB_NAME)
                .addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            println(db)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}