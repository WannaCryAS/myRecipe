package com.wannacry.myrecipe.repository

import androidx.lifecycle.LiveData
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.db.MyRecipeDatabase

class RecipeRepository(private val db: MyRecipeDatabase) {
    suspend fun addRecipe(meal: Meal) = db.getRecipeDAO().addRecipe(meal)
    suspend fun updateRecipe(meal: Meal) = db.getRecipeDAO().updateRecipe(meal) //TODO: will update for edit function
    suspend fun deleteRecipe(id: String) = db.getRecipeDAO().deleteRecipe(id)
    fun getAllRecipe(): LiveData<List<Meal>> = db.getRecipeDAO().getAllRecipe()
}