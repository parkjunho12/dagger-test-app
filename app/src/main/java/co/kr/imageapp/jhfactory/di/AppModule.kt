package co.kr.imageapp.jhfactory.di

import android.content.Context
import androidx.room.Room
import co.kr.imageapp.jhfactory.data.local.mypage.ImageDao
import co.kr.imageapp.jhfactory.data.local.mypage.ImageDataBase
import co.kr.imageapp.jhfactory.data.local.search.SearchDao
import co.kr.imageapp.jhfactory.data.local.search.SearchDataBase
import co.kr.imageapp.jhfactory.util.Network
import co.kr.imageapp.jhfactory.util.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ImageDataBase {
        return Room.databaseBuilder(context, ImageDataBase::class.java, "image_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMainDao(dataBase: ImageDataBase): ImageDao {
        return dataBase.imageDao()
    }
    @Provides
    @Singleton
    fun provideSearchDatabase(@ApplicationContext context: Context): SearchDataBase {
        return Room.databaseBuilder(context, SearchDataBase::class.java, "search_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(dataBase: SearchDataBase): SearchDao {
        return dataBase.searchDao()
    }


    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }
}