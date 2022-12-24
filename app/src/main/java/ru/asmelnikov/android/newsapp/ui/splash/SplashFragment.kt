package ru.asmelnikov.android.newsapp.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentDetailsBinding
import ru.asmelnikov.android.newsapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val nBinding get() = _binding //!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return nBinding?.root // ??
    }
}