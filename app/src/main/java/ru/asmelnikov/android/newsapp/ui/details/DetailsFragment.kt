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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.databinding.FragmentDetailsBinding

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val nBinding get() = _binding
    private val bundleArgs: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return nBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleArg = bundleArgs.article
        articleArg.let { article ->
            article.urlToImage?.let {
                Glide.with(this).load(article.urlToImage).error(R.drawable.ic_image)
                    .into(nBinding!!.headerImage)
            }
            nBinding?.headerImage?.clipToOutline = true
            nBinding?.articleDetailsTitle?.text = article.title
            nBinding?.articleText?.text = article.description

            nBinding?.articleDetailsButton?.setOnClickListener {
                try {
                    Intent()
                        .setAction(Intent.ACTION_VIEW)
                        .addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse(takeIf {
                            URLUtil.isValidUrl(article.url)
                        }.let {
                            article.url
                        } ?: "https://google.com"))
                        .let {
                            ContextCompat.startActivity(requireContext(), it, null)
                        }
                } catch (e: Exception) {
                    Toast.makeText(context, "Don't have any browser", Toast.LENGTH_LONG).show()
                }
            }
            nBinding?.iconFavorite?.setOnClickListener {
                viewModel.saveFavoriteArticle(article)
            }
        }
    }
}