package com.wannacry.myrecipe.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wannacry.myrecipe.repository.FavoriteMealRepository

class MealViewModelFactory(
    private val favoriteMealRepository: FavoriteMealRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(favoriteMealRepository) as T
    }
}