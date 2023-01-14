package ru.asmelnikov.android.newsapp.ui.favorite

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentFavoriteBinding
import ru.asmelnikov.android.newsapp.ui.adapters.NewsAdapter
import ru.asmelnikov.android.newsapp.utils.NetworkUtils

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val nBinding get() = _binding
    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return nBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        NetworkUtils.getNetworkLiveData(requireContext())
            .observe(viewLifecycleOwner) { isConnected ->
                if (!isConnected) {
                    favorite_news_adapter.visibility = View.GONE
                    favorites_count.visibility = View.GONE
                    invisible_layout.visibility = View.GONE
                    no_internet.visibility = View.VISIBLE
                    Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_LONG).show()
                } else {
                    favorite_news_adapter.visibility = View.VISIBLE
                    favorites_count.visibility = View.VISIBLE
                    invisible_layout.visibility = View.VISIBLE
                    no_internet.visibility = View.GONE
                    viewModel.getCount().observe(
                        viewLifecycleOwner
                    ) { count ->
                        if (count > 99) {
                            favorites_count.text = getString(R.string.over_99)
                            nBinding?.invisibleLayout?.visibility = View.GONE
                            viewModel.getAllFavorites().observe(viewLifecycleOwner) { articles ->
                                newsAdapter.differ.submitList(articles.asReversed())
                            }
                        } else if (count == 0) {
                            favorites_count.text = count.toString()
                            nBinding?.invisibleLayout?.visibility = View.VISIBLE
                        } else {
                            favorites_count.text = count.toString()
                            nBinding?.invisibleLayout?.visibility = View.GONE
                            viewModel.getAllFavorites().observe(viewLifecycleOwner) { articles ->
                                newsAdapter.differ.submitList(articles.asReversed())
                            }
                        }
                    }
                }
            }

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
                R.id.action_favoriteFragment_to_detailsFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteFavoriteArticle(article)
                Snackbar.make(view, R.string.successfully_deleted, Snackbar.LENGTH_SHORT).apply {
                    setAction(R.string.undo) {
                        viewModel.saveFavoriteArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(favorite_news_adapter)
        }


        nBinding?.invisibleButton?.setOnClickListener {
            view.findNavController().navigate(R.id.action_favoriteFragment_to_mainFragment)
        }

    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter(requireContext())
        favorite_news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}