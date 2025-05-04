package com.dicoding.forumdiskusiapp.di

import com.dicoding.forumdiskusiapp.data.repository.AppRepository

object Injection {
    fun provideRepository() : AppRepository {
        return AppRepository.getInstance()
    }
}