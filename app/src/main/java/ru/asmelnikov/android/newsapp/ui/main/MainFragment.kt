package ru.asmelnikov.android.newsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentMainBinding
import ru.asmelnikov.android.newsapp.ui.adapters.NewsAdapter
import ru.asmelnikov.android.newsapp.utils.NetworkUtils
import ru.asmelnikov.android.newsapp.utils.Resource

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val nBinding get() = _binding

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return nBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        newsAdapter.setOnItemClickListenerShared {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, it.url)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    it.title
                )
            }.also { intent ->
                val chooseIntent = Intent.createChooser(
                    intent, getString(R.string.send_article)
                )
                startActivity(chooseIntent)
            }
        }

        newsAdapter.setOnItemClickListener {
            val bundle = bundleOf("article" to it)
            view.findNavController().navigate(
                R.id.action_mainFragment_to_detailsFragment,
                bundle
            )
        }

        NetworkUtils.getNetworkLiveData(requireContext())
            .observe(viewLifecycleOwner) { isConnected ->
                if (!isConnected) {
                    no_internet.visibility = View.VISIBLE
                    news_adapter.visibility = View.GONE
                    Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show()
                } else {
                    no_internet.visibility = View.GONE
                    news_adapter.visibility = View.VISIBLE
                    viewModel.newsLiveData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Success -> {
                                progress_bar.visibility = View.INVISIBLE
                                response.data.let {
                                    newsAdapter.differ.submitList(it?.articles)
                                }
                            }
                            is Resource.Error -> {
                                progress_bar.visibility = View.INVISIBLE
                                response.data.let {
                                    Toast.makeText(
                                        activity, R.string.no_internet,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            is Resource.Loading -> {
                                progress_bar.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter(requireContext())
        news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}