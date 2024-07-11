package com.example.bhagavadgita.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bhagavadgita.databinding.ItemViewVerseBinding
import com.example.bhagavadgita.model.VersesItem

class AdapterVerses(val onVerseItemClicked: (String, Int) -> Unit) : RecyclerView.Adapter<AdapterVerses.VersesViewHolder>() {
    inner class VersesViewHolder(val binding: ItemViewVerseBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<VersesItem>() {
        override fun areItemsTheSame(oldItem: VersesItem, newItem: VersesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VersesItem, newItem: VersesItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersesViewHolder {
        return VersesViewHolder(
            ItemViewVerseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: VersesViewHolder, position: Int) {
        val verse = differ.currentList[position]
        holder.binding.tvVerseNumber.text = "श्लोक ${verse.verse_number}"

        // Find the translation by Swami Tejomayananda in Hindi
        val tejomayanandaTranslation = verse.translations.find {
            it.author_name == "Swami Tejomayananda" && it.language == "hindi"
        }

        // Set the Hindi translation if found, otherwise set an empty string or a placeholder
        holder.binding.tvVerse.text = tejomayanandaTranslation?.description ?: "Translation not available"

        // Set the Sanskrit text
        holder.binding.verseSanskrit.text = verse.text

        // Set click listener
        holder.itemView.setOnClickListener {
            onVerseItemClicked(verse.text, verse.verse_number)
        }
    }
}