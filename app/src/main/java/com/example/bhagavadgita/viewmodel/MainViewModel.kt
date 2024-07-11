package com.example.bhagavadgita.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bhagavadgita.model.ChaptersItem
import com.example.bhagavadgita.model.VersesItem
import com.example.bhagavadgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {
    val appRepository = AppRepository()

    fun getAllChapter(): Flow<List<ChaptersItem>> = appRepository.getAllChapter()
    fun getVerses(chapterNumber:Int) : Flow<List<VersesItem>> = appRepository.getVerses(chapterNumber)
    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VersesItem> = appRepository.getParticularVerse(chapterNumber, verseNumber)
}