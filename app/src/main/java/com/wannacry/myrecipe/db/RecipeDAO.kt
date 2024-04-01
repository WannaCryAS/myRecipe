package com.wannacry.myrecipe.db;

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wannacry.myrecipe.data.Meal

@Dao
interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(meal: Meal)

    @Query("DELETE FROM recipe_db WHERE idMeal = :id")
    suspend fun deleteRecipe(id: String)

    @Update
    suspend fun updateRecipe(meal: Meal)

    @Query("SELECT * FROM recipe_db")
    fun getAllRecipe(): LiveData<List<Meal>>
}