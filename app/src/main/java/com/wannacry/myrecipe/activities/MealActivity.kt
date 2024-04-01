package com.wannacry.myrecipe.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.databinding.ActivityMealBinding
import com.wannacry.myrecipe.db.MyRecipeDatabase
import com.wannacry.myrecipe.repository.FavoriteMealRepository
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB
import com.wannacry.myrecipe.viewModel.MealViewModel
import com.wannacry.myrecipe.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var activityMealBinding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel
    private var mealId: String = ""
    private var mealName: String = ""
    private var mealThumb: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMealBinding = ActivityMealBinding.inflate(layoutInflater)
        val contentView = activityMealBinding.root
        setContentView(contentView)

        val myRecipeDatabase = MyRecipeDatabase.getInstance(this)
        val favoriteMealRepository = FavoriteMealRepository(myRecipeDatabase)
        val mealViewModelFactory = MealViewModelFactory(favoriteMealRepository)
        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()

        setUpMealInfoToViews()

        onLoadingState()

        mealViewModel.getMealDetailsById(mealId)

        observeMealDetails()
    }

    private fun getMealInfoFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(MEAL_ID)!!
        mealName = intent.getStringExtra(MEAL_NAME)!!
        mealThumb = intent.getStringExtra(MEAL_THUMB)!!
    }

    private fun setUpMealInfoToViews(){
        Glide
            .with(this)
            .load(mealThumb)
            .centerCrop()
            .placeholder(R.drawable.img_meal_placeholder)
            .error(R.drawable.img_error)
            .into(activityMealBinding.ivMeal)

        activityMealBinding.ctlMeal.title = mealName
        activityMealBinding.ctlMeal.setExpandedTitleColor(getColor(R.color.white))
        activityMealBinding.ctlMeal.setCollapsedTitleTextColor(getColor(R.color.white))
    }

    private fun observeMealDetails() {
        mealViewModel.observeMealDetailsLiveData().observe(this){meal ->
            onResponseState()
            setupMealDetailsToViews(meal)
            onAddToFavoriteButtonClicked(meal)
        }
    }

    private fun setupMealDetailsToViews(meal: Meal) {
        activityMealBinding.tvMealCategory.text = "Category: ${meal.strCategory}"
        activityMealBinding.tvMealArea.text = "Area: ${meal.strArea}"
        activityMealBinding.tvMealInstructions.text = meal.strInstructions
    }

    private fun onLoadingState() {
        activityMealBinding.progressBar.visibility = View.VISIBLE
        activityMealBinding.fabAddToFavorites.visibility = View.INVISIBLE
        activityMealBinding.tvMealInstructionsTitle.visibility = View.INVISIBLE
        activityMealBinding.tvMealInstructions.visibility = View.INVISIBLE
    }

    private fun onResponseState() {
        activityMealBinding.progressBar.visibility = View.INVISIBLE
        activityMealBinding.fabAddToFavorites.visibility = View.VISIBLE
        activityMealBinding.tvMealInstructionsTitle.visibility = View.VISIBLE
        activityMealBinding.tvMealInstructions.visibility = View.VISIBLE
    }

    private fun onAddToFavoriteButtonClicked(meal: Meal) {
        activityMealBinding.fabAddToFavorites.setOnClickListener {
            mealViewModel.addMealToFavorites(meal)
            Snackbar
                .make(activityMealBinding.root, "Successfully saved to Favorites", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getColor(R.color.light_accent))
                .setTextColor(getColor(R.color.off_white))
                .show()
        }
    }
}