package com.wannacry.myrecipe.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.adapter.RecyclerViewAdapter
import com.wannacry.myrecipe.data.MealByCategory
import com.wannacry.myrecipe.databinding.ActivityCategoryMealsBinding
import com.wannacry.myrecipe.databinding.LayoutMealItemBinding
import com.wannacry.myrecipe.fragments.HomeFragment
import com.wannacry.myrecipe.utils.Contants.CATEGORY_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB
import com.wannacry.myrecipe.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var activityCategoryMealsBinding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsRecyclerViewAdapter: RecyclerViewAdapter<MealByCategory>
    private lateinit var layoutMealItemBinding: LayoutMealItemBinding
    private var categoryName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCategoryMealsBinding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        val contentView = activityCategoryMealsBinding.root
        setContentView(contentView)

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        getCategoryInfoFromIntent()

        categoryMealsViewModel.getMealsByCategory(categoryName)

        observeCategoryMeals()
    }

    private fun getCategoryInfoFromIntent() {
        val intent = intent
        categoryName = intent.getStringExtra(CATEGORY_NAME)!!
    }

    private fun observeCategoryMeals() {
        categoryMealsViewModel.observeCategoryMealsLiveData().observe(this){ mealsByCategory ->
            val categoryMealsCountDisplayText = "$categoryName: ${mealsByCategory.size}"
            activityCategoryMealsBinding.tvCategoryMealsCount.text = categoryMealsCountDisplayText
            setUpCategoryMealsRecyclerView(mealsByCategory)
        }
    }

    private fun setUpCategoryMealsRecyclerView(mealsByCategory: List<MealByCategory>) {
        val verticalGridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        categoryMealsRecyclerViewAdapter = RecyclerViewAdapter(R.layout.layout_meal_item, mealsByCategory, true){ view, mealByCategory, _ ->
            layoutMealItemBinding = LayoutMealItemBinding.bind(view)
            layoutMealItemBinding.tvMealName.text = mealByCategory.strMeal
            Glide
                .with(this)
                .load(mealByCategory.strMealThumb)
                .centerCrop()
                .placeholder(R.drawable.img_meal_placeholder)
                .error(R.drawable.img_error)
                .into(layoutMealItemBinding.ivMealImage)
            onCategoryMealClicked(mealByCategory)
        }
        activityCategoryMealsBinding.rvCategoryMeals.apply {
            adapter = categoryMealsRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = verticalGridLayoutManager
        }
    }

    private fun onCategoryMealClicked(mealByCategory: MealByCategory){
        layoutMealItemBinding.clMealItem.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, mealByCategory.idMeal)
                putExtra(MEAL_NAME, mealByCategory.strMeal)
                putExtra(MEAL_THUMB, mealByCategory.strMealThumb)
            }
            startActivity(intent)
        }
    }
}