package com.mohamednader.shoponthego.Utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface

class GenericViewModelFactory(private val repo: RepositoryInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            val constructor = modelClass.getConstructor(RepositoryInterface::class.java)
            constructor.newInstance(repo)
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}