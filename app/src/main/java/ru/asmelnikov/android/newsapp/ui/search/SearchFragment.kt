package ru.asmelnikov.android.newsapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.no_internet_banner.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentSearchBinding
import ru.asmelnikov.android.newsapp.ui.adapters.NewsAdapter
import ru.asmelnikov.android.newsapp.utils.NetworkUtils
import ru.asmelnikov.android.newsapp.utils.Resource

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val nBinding get() = _binding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return nBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        newsAdapter.setOnItemClickListener {
            val bundle = bundleOf("article" to it)
            view.findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment,
                bundle
            )
        }
        newsAdapter.setOnItemClickListenerShared {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, it.url)
                putExtra(
                    Intent.EXTRA_SUBJECT, it.title
                )
            }.also { intent ->
                val chooseIntent = Intent.createChooser(
                    intent, getString(R.string.send_article)
                )
                startActivity(chooseIntent)
            }
        }

        NetworkUtils.getNetworkLiveData(requireContext())
            .observe(viewLifecycleOwner) { isConnected ->
                if (!isConnected) {
                    no_internet.visibility = View.VISIBLE
                    search_news_adapter.visibility = View.GONE
                    edit_search.visibility = View.GONE
                    Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show()
                } else {
                    no_internet.visibility = View.GONE
                    search_news_adapter.visibility = View.VISIBLE
                    edit_search.visibility = View.VISIBLE


                    var job: Job? = null
                    edit_search.addTextChangedListener { text: Editable? ->
                        job?.cancel()
                        job = MainScope().launch {
                            delay(500L)
                            text?.let {
                                if (it.toString().isNotEmpty()) {
                                    viewModel.getSearchNews(query = it.toString())
                                }
                            }
                        }
                    }

                    viewModel.searchNewsLiveData.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is Resource.Success -> {
                                search_progress_bar.visibility = View.INVISIBLE
                                response.data.let {
                                    newsAdapter.differ.submitList(it?.articles)
                                }
                            }
                            is Resource.Error -> {
                                search_progress_bar.visibility = View.INVISIBLE
                                response.data.let {
                                    Toast.makeText(
                                        activity, R.string.enter_query,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            is Resource.Loading -> {
                                search_progress_bar.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        search_news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}