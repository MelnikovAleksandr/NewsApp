package ru.asmelnikov.android.newsapp.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.models.Article
import ru.asmelnikov.android.newsapp.utils.loadImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewsAdapter(private val context: Context) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val callBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            article_image.loadImage(article.urlToImage.toString())
            article_image.clipToOutline = true
            article_title.text = article.title
            val formattedDate = LocalDateTime
                .parse(article.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern(context.getString(R.string.data_pattern)))
            article_date.text = formattedDate

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
            shared_article.setOnClickListener {
                onItemClickListenerShared?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    private var onItemClickListenerShared: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemClickListenerShared(listener: (Article) -> Unit) {
        onItemClickListenerShared = listener
    }
}