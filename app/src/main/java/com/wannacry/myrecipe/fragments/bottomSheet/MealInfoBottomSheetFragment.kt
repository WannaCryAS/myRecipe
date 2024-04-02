package com.wannacry.myrecipe.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wannacry.myrecipe.activities.MealActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.databinding.FragmentMealInfoBottomSheetBinding
import com.wannacry.myrecipe.utils.Contants.MEAL_AREA
import com.wannacry.myrecipe.utils.Contants.MEAL_CATEGORY
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB

class MealInfoBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealArea: String? = null
    private var mealCategory: String? = null
    private var mealThumb: String? = null
    private var _mealBottomSheetBinding: FragmentMealInfoBottomSheetBinding? = null
    private val mealBottomSheetBinding get() = _mealBottomSheetBinding!!

    companion object {
        fun newInstance(
            mealId: String,
            mealName: String?,
            mealArea: String?,
            mealCategory: String?,
            mealThumb: String?
        ) =
            MealInfoBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, mealId)
                    putString(MEAL_NAME, mealName)
                    putString(MEAL_AREA, mealArea)
                    putString(MEAL_CATEGORY, mealCategory)
                    putString(MEAL_THUMB, mealThumb)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
            mealName = it.getString(MEAL_NAME)
            mealArea = it.getString(MEAL_AREA)
            mealCategory = it.getString(MEAL_CATEGORY)
            mealThumb = it.getString(MEAL_THUMB)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mealBottomSheetBinding =
            FragmentMealInfoBottomSheetBinding.inflate(inflater, container, false)
        return mealBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMealInfoToViews()
        onReadMoreClicked()
    }

    private fun setupMealInfoToViews() {
        mealBottomSheetBinding.tvBottomSheetMealName.text = mealName
        mealBottomSheetBinding.tvBottomSheetMealArea.text = mealArea
        mealBottomSheetBinding.tvBottomSheetMealCategory.text = mealCategory
        Glide
            .with(this@MealInfoBottomSheetFragment)
            .load(mealThumb)
            .centerCrop()
            .placeholder(R.drawable.img_meal_placeholder)
            .error(R.drawable.img_error)
            .into(mealBottomSheetBinding.ivBottomSheetMeal)
    }

    private fun onReadMoreClicked() {
        mealBottomSheetBinding.tvBottomSheetReadMore.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, mealId)
                putExtra(MEAL_NAME, mealName)
                putExtra(MEAL_THUMB, mealThumb)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mealBottomSheetBinding = null
    }
}