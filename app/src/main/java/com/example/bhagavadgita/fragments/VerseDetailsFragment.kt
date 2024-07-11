package com.example.bhagavadgita.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bhagavadgita.R
import com.example.bhagavadgita.databinding.FragmentVerseDetailsBinding
import com.example.bhagavadgita.model.Commentary
import com.example.bhagavadgita.model.Translation
import com.example.bhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VerseDetailsFragment : Fragment() {
    private lateinit var binding: FragmentVerseDetailsBinding
    private val viewModel: MainViewModel by viewModels()
    private var chapNumber = 0
    private var verseNumber = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerseDetailsBinding.inflate(layoutInflater)
        changeStatusBarColor()
        getAndSetChapterNumber()
        getVerseDetail()
        setupReadMoreButton()
        return binding.root
    }

    private fun setupReadMoreButton() {
        var isExpanded = false
        binding.tvSeeMoreDetail.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                binding.tvTextCommentry.maxLines = Integer.MAX_VALUE
                binding.tvTextCommentry.ellipsize = null
                binding.tvSeeMoreDetail.text = "Read Less"
            } else {
                binding.tvTextCommentry.maxLines = 4
                binding.tvTextCommentry.ellipsize = TextUtils.TruncateAt.END
                binding.tvSeeMoreDetail.text = "Read More..."
            }
        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {
            viewModel.getParticularVerse(chapNumber, verseNumber).collect {
                binding.tvVersetext.text = it.text
                binding.tvTranslateIfEnglish.text = it.transliteration
                binding.tvWordIfEnglish.text = it.word_meanings
                var translationList = arrayListOf<Translation>()
                for (i in translationList) {
                    translationList.add(i)
                }
                val translationListSize = translationList.size
                if (translationList.isNotEmpty()) {
                    binding.tvAuthorName.text = translationList[0].author_name
                    binding.tvText.text = translationList[0].description
                    if (translationListSize == 1) binding.fabTranslationRight.visibility = View.GONE
                    var i = 0
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < translationListSize - 1) {
                            i++
                            binding.tvAuthorName.text = translationList[i].author_name
                            binding.tvText.text = translationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE
                            if (i == translationListSize - 1) binding.fabTranslationRight.visibility =
                                View.GONE
                        }
                    }

                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = translationList[i].author_name
                            binding.tvText.text = translationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE
                            if (i == 0) binding.fabTranslationLeft.visibility = View.GONE
                        }
                    }
                }
                val commentryList = arrayListOf<Commentary>()
                for (i in it.commentaries) {
                    commentryList.add(i)
                }
                val commentaryListSize = commentryList.size
                if (commentryList.isNotEmpty()) {
                    binding.tvAuthorCommentryName.text = commentryList[0].author_name
                    binding.tvTextCommentry.text = commentryList[0].description
                    if (commentaryListSize == 1) binding.fabCommentryRight.visibility = View.GONE
                    var i = 0
                    binding.fabCommentryRight.setOnClickListener {
                        if (i < commentaryListSize - 1) {
                            i++
                            binding.tvAuthorCommentryName.text = commentryList[i].author_name
                            binding.tvTextCommentry.text = commentryList[i].description
                            binding.fabCommentryLeft.visibility = View.VISIBLE
                            if (i == commentaryListSize - 1) binding.fabCommentryRight.visibility =
                                View.GONE
                        }
                    }
                    binding.fabCommentryLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorCommentryName.text = commentryList[i].author_name
                            binding.tvTextCommentry.text = commentryList[i].description
                            binding.fabCommentryRight.visibility = View.VISIBLE
                            if (i == 0) binding.fabCommentryLeft.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getAndSetChapterNumber() {
        val bundle = arguments
        chapNumber = bundle?.getInt("chapNum")!!
        verseNumber = bundle.getInt("verseNum")
        binding.tvVerseNumber.text = "||$chapNumber.$verseNumber||"
    }

    private fun changeStatusBarColor() {
        activity?.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.pitambar)
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                true
        }
    }

}