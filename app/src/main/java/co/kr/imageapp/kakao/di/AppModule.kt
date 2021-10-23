package co.kr.imageapp.kakao.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import co.kr.imageapp.kakao.data.local.mypage.ImageDao
import co.kr.imageapp.kakao.data.local.mypage.ImageDataBase
import co.kr.imageapp.kakao.util.Network
import co.kr.imageapp.kakao.util.NetworkConnectivity
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
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }
}