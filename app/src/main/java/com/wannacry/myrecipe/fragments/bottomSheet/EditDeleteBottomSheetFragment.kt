package com.wannacry.myrecipe.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.wannacry.myrecipe.R
import com.wannacry.myrecipe.activities.MainActivity
import com.wannacry.myrecipe.activities.MealActivity
import com.wannacry.myrecipe.data.Meal
import com.wannacry.myrecipe.databinding.FragmentEditDeleteBottomSheetBinding
import com.wannacry.myrecipe.utils.Contants.MEAL_AREA
import com.wannacry.myrecipe.utils.Contants.MEAL_CATEGORY
import com.wannacry.myrecipe.utils.Contants.MEAL_ID
import com.wannacry.myrecipe.utils.Contants.MEAL_NAME
import com.wannacry.myrecipe.utils.Contants.MEAL_THUMB
import com.wannacry.myrecipe.viewModel.MainViewModel

class EditDeleteBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private var mealName: String? = null
    private var mealArea: String? = null
    private var mealCategory: String? = null
    private var mealThumb: String? = null
    private lateinit var mainViewModel: MainViewModel
    private var _editDeleteBottomSheetBinding: FragmentEditDeleteBottomSheetBinding? = null
    private val editDeleteBottomSheetBinding get() = _editDeleteBottomSheetBinding!!

    companion object {
        @JvmStatic
        fun newInstance(mealId: String, mealName: String?, mealArea: String?, mealCategory: String?, mealThumb: String?) =
            EditDeleteBottomSheetFragment().apply {
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
        mainViewModel = (activity as MainActivity).mainViewModel
        arguments?.let {
            mealId = it.getString(MEAL_ID)
            mealName = it.getString(MEAL_NAME)
            mealArea = it.getString(MEAL_AREA)
            mealCategory = it.getString(MEAL_CATEGORY)
            mealThumb = it.getString(MEAL_THUMB)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _editDeleteBottomSheetBinding = FragmentEditDeleteBottomSheetBinding.inflate(inflater, container, false)
        return editDeleteBottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMealInfoToViews()
        onReadMoreClicked()
        onBottonClicked()
    }

    private fun setupMealInfoToViews(){
        editDeleteBottomSheetBinding.tvBottomSheetMealName.text = mealName
        editDeleteBottomSheetBinding.tvBottomSheetMealArea.text = mealArea
        editDeleteBottomSheetBinding.tvBottomSheetMealCategory.text = mealCategory
        Glide.with(this@EditDeleteBottomSheetFragment)
            .load(mealThumb)
            .centerCrop()
            .placeholder(R.drawable.img_meal_placeholder)
            .error(R.drawable.img_error)
            .into(editDeleteBottomSheetBinding.ivBottomSheetMeal)
    }

    private fun onReadMoreClicked(){
        editDeleteBottomSheetBinding.tvBottomSheetReadMore.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(MEAL_ID, mealId)
                putExtra(MEAL_NAME, mealName)
                putExtra(MEAL_THUMB, mealThumb)
            }
            startActivity(intent)
            onDestroyView()
        }
    }

    private fun onBottonClicked() {
        editDeleteBottomSheetBinding.btnDelete.setOnClickListener {

            mealId?.let { it1 -> mainViewModel.deleteRecipe(it1) }
            onDestroyView()
                Snackbar
                    .make(requireView(), "Successfully removed from Favorites", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(requireContext().getColor(R.color.light_accent))
                    .setTextColor(requireContext().getColor(R.color.off_white))
                    .setActionTextColor(requireContext().getColor(R.color.accent))
                    .show()

            onDestroyView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _editDeleteBottomSheetBinding = null
    }

}