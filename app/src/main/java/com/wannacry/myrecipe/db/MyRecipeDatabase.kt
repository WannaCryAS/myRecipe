package com.wannacry.myrecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wannacry.myrecipe.data.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(RecipeTypeConverter::class)
abstract class MyRecipeDatabase: RoomDatabase(){
    abstract fun getFavoriteMealDAO(): FavoriteMealDAO
    abstract fun getRecipeDAO(): RecipeDAO
    companion object {
        @Volatile
        private var INSTANCE: MyRecipeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MyRecipeDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, MyRecipeDatabase::class.java, "recipe_db").fallbackToDestructiveMigration().build()
            }
            return INSTANCE as MyRecipeDatabase
        }
    }
}