package com.example.bhagavadgita.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bhagavadgita.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeStatusBarColor()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // Start animations
        startAnimations(view)

        // Navigate to home fragment after delay of 3s
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, 3000)

        return view
    }

    //Animation Splash of 1s
    private fun startAnimations(view: View) {
        // Fade in animation for the entire layout
        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        fadeIn.duration = 1000 // 1 second

        // Scale animation for the logo (assuming you have an ImageView with id 'logoImageView')
        val logo = view.findViewById<View>(R.id.logoImageView)
        val scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1f)

        val scaleAnimator = AnimatorSet()
        scaleAnimator.playTogether(scaleX, scaleY)
        scaleAnimator.duration = 1000 // 1 second
        scaleAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Play animations together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeIn, scaleAnimator)
        animatorSet.start()
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