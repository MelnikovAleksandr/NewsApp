package ru.asmelnikov.android.newsapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentDetailsBinding
import ru.asmelnikov.android.newsapp.utils.loadImage

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val nBinding get() = _binding!!
    private val bundleArgs: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return nBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleArg = bundleArgs.article
        articleArg.let { article ->
            article.urlToImage?.let {
                nBinding.headerImage.loadImage(article.urlToImage)
            }
            nBinding.headerImage.clipToOutline = true
            nBinding.articleDetailsTitle.text = article.title
            nBinding.articleText.text = article.description
            nBinding.articleDetailsButton.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf {
                            URLUtil.isValidUrl(article.url)
                        }.let {
                            article.url
                        } ?: getString(R.string.empty_url)))
                        .let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.no_browser, Toast.LENGTH_LONG).show()
                }
            }
            nBinding.iconFavorite.setOnClickListener {
                viewModel.favoriteAddAndCheck(bundleArgs.article)
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            viewModel.favoritesCheck(articleArg)
            if (it == 0) {
                nBinding.iconFavorite.setImageResource(R.drawable.ic_favorite_2)
            } else {
                nBinding.iconFavorite.setImageResource(R.drawable.ic_favorite_add)
            }
        }

        nBinding.iconShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, articleArg.url)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    articleArg.title
                )
            }.also { intent ->
                val chooseIntent = Intent.createChooser(
                    intent, getString(R.string.send_article)
                )
                startActivity(chooseIntent)
            }
        }

        nBinding.iconBack.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }
}