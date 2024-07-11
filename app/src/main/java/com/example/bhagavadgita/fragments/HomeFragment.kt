package com.example.bhagavadgita.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bhagavadgita.NetworkManager
import com.example.bhagavadgita.R
import com.example.bhagavadgita.adapter.AdapterChapter
import com.example.bhagavadgita.databinding.FragmentHomeBinding
import com.example.bhagavadgita.model.ChaptersItem
import com.example.bhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val viewMode: MainViewModel by viewModels()
    private var adapterChapter = AdapterChapter(::onChapterIVClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        changeStatusBarColor()
        checkInternetConnectivity()
        _binding.btnRetry.setOnClickListener {
            checkInternetConnectivity()
        }
        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        _binding.shimmer.startAnimation(scaleUp)
        // Inflate the layout for this fragment
        return _binding.root
    }




    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true){
                getAllChaapter()
                _binding.rvChapters.visibility = View.VISIBLE
                _binding.shimmer.visibility = View.VISIBLE
                _binding.errorMessageContainer.visibility = View.GONE
                _binding.tvChapters.visibility = View.VISIBLE
            }
            else{
                _binding.shimmer.visibility = View.GONE
                _binding.rvChapters.visibility = View.GONE
                _binding.errorMessageContainer.visibility = View.VISIBLE
                _binding.tvChapters.visibility = View.GONE
            }
        }
    }

    private fun getAllChaapter() {

        lifecycleScope.launch {

            viewMode.getAllChapter().collect {
                Log.d("TAG", "getAllChaapter: $it")
                adapterChapter = AdapterChapter(::onChapterIVClicked)
                _binding.rvChapters.adapter = adapterChapter
                adapterChapter.differ.submitList(it)
                _binding.shimmer.visibility = View.GONE
            }
        }

    }

    private fun onChapterIVClicked(chaptersItem: ChaptersItem){

        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putString("chapterTitle", chaptersItem.name)
        bundle.putString("chapterDescription", chaptersItem.chapter_summary_hindi)
        bundle.putInt("chapterVerseCount", chaptersItem.verses_count)
        findNavController().navigate(R.id.action_homeFragment_to_verseFragment, bundle)
    }

    private fun changeStatusBarColor() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.pitambar)
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                true
        }
    }
}