package com.app.notely.core.di

import com.app.notely.data.repository.NoteRepositoryImpl
import com.app.notely.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideNoteRepository(impl: NoteRepositoryImpl): NoteRepository = impl
}