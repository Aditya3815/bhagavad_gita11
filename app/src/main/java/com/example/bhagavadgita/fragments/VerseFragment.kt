package com.example.bhagavadgita.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bhagavadgita.NetworkManager
import com.example.bhagavadgita.R
import com.example.bhagavadgita.adapter.AdapterVerses
import com.example.bhagavadgita.databinding.FragmentVerseBinding
import com.example.bhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import android.animation.ValueAnimator
import android.text.TextUtils
import androidx.core.animation.doOnEnd
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class VerseFragment : Fragment() {

    private lateinit var binding: FragmentVerseBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapterVerses: AdapterVerses
    private var chapterNumber: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeNetworkState()
        getAndSetChapterDetail()
        getAllVerses()
        onScrollListener()
    }

    private fun setupUI() {
        changeStatusBarColor()
        setupReadMoreButton()
        adapterVerses = AdapterVerses(::onVerseItemClicked)
        binding.rvVerses.adapter = adapterVerses
    }

    private fun onVerseItemClicked(verse: String, verseNumber: Int) {
        val bundle = Bundle()
        bundle.putInt("chapNum", chapterNumber)
        bundle.putInt("verseNum", verseNumber)
        Log.d("VerseFragment", "Clicked verse $verseNumber: $verse")
        findNavController().navigate(R.id.action_verseFragment_to_verseDetailsFragment)
    }

    private fun observeNetworkState() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) { isConnected ->
            updateUIForNetworkState(isConnected)
        }
    }

    private fun updateUIForNetworkState(isConnected: Boolean) {
        if (isConnected) {
            binding.rvVerses.visibility = View.VISIBLE
            binding.shimmer.visibility = View.VISIBLE
            binding.errorMessageContainer.visibility = View.GONE
            getAllVerses()
        } else {
            binding.shimmer.visibility = View.GONE
            binding.rvVerses.visibility = View.GONE
            binding.errorMessageContainer.visibility = View.VISIBLE
        }
    }

    private fun onScrollListener() {
        binding.rvVerses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { // Scrolling down
                    binding.tvChapterDescription.maxLines = 4
                    binding.tvChapterDescription.ellipsize = TextUtils.TruncateAt.END
                    binding.tvSeeMore.text = "Read More..."
                }
            }
        })
    }


    private fun setupReadMoreButton() {
        var isExpanded = false
        binding.tvSeeMore.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                binding.tvChapterDescription.maxLines = Integer.MAX_VALUE
                binding.tvChapterDescription.ellipsize = null
                binding.tvSeeMore.text = "Read Less"
            } else {
                binding.tvChapterDescription.maxLines = 4
                binding.tvChapterDescription.ellipsize = TextUtils.TruncateAt.END
                binding.tvSeeMore.text = "Read More..."
            }
        }
    }




    private fun getAndSetChapterDetail() {
        arguments?.let { bundle ->
            chapterNumber = bundle.getInt("chapterNumber")
            binding.tvChapterNumber.text = "अध्याय $chapterNumber"
            binding.tvChapterName.text = bundle.getString("chapterTitle")
            binding.tvChapterDescription.text = bundle.getString("chapterDescription")
            binding.tvNumberOfVerses.text =  bundle.getInt("chapterVerseCount").toString()
        }
    }

    private fun getAllVerses() {
        lifecycleScope.launch {
            viewModel.getVerses(chapterNumber).collect { verses ->
                adapterVerses.differ.submitList(verses)
                binding.shimmer.visibility = View.GONE
                Log.d("VerseFragment", "Received ${verses.size} verses")
            }
        }
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