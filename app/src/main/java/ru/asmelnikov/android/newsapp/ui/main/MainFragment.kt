package ru.asmelnikov.android.newsapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentDetailsBinding
import ru.asmelnikov.android.newsapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val nBinding get() = _binding //!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return nBinding?.root // ??
    }
}