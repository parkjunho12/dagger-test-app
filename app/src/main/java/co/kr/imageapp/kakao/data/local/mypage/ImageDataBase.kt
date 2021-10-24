package co.kr.imageapp.kakao.data.local.mypage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import co.kr.imageapp.kakao.data.dto.mypage.ImageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ImageData::class], version = 7)
abstract class ImageDataBase : RoomDatabase(){
    abstract fun imageDao(): ImageDao

    companion object {
        private const val IMAGE_DB_NAME = "image_db"

        fun getInstance(context: Context) : ImageDataBase {
            return buildDatabase(context)
        }

        private fun buildDatabase(context: Context): ImageDataBase {
            return Room.databaseBuilder(context, ImageDataBase::class.java, IMAGE_DB_NAME)
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