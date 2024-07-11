package com.example.bhagavadgita.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bhagavadgita.databinding.ItemViewChaptersBinding
import com.example.bhagavadgita.model.ChaptersItem

class AdapterChapter(var onChapterIVClicked: (ChaptersItem) -> Unit) : RecyclerView.Adapter<AdapterChapter.ChaptersViewHolder>() {
    inner class ChaptersViewHolder(val binding: ItemViewChaptersBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<ChaptersItem>() {
        override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        return ChaptersViewHolder(
            ItemViewChaptersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter = differ.currentList[position]
        holder.binding.apply {
            tvChapterNumber.text = "अध्याय ${chapter.chapter_number}"
            tvChapterName.text = chapter.name
            tvChapterDescription.text = chapter.chapter_summary_hindi
            tvVerseCount.text = chapter.verses_count.toString()
            tvVerse.text = "श्लोक"
        }

        holder.binding.ll.setOnClickListener{
            onChapterIVClicked(chapter)
        }
    }


}