package com.safestoragecleaner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.safestoragecleaner.R
import com.safestoragecleaner.model.FileItem

class FileAdapter(
    private val files: MutableList<FileItem>
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    class FileViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val fileName: TextView =
            itemView.findViewById(R.id.fileName)

        val fileSize: TextView =
            itemView.findViewById(R.id.fileSize)

        val checkBox: CheckBox =
            itemView.findViewById(R.id.fileCheckBox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FileViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_file,
            parent,
            false
        )

        return FileViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FileViewHolder,
        position: Int
    ) {

        val file = files[position]

        holder.fileName.text = file.name

        holder.fileSize.text =
            formatSize(file.size)

        holder.checkBox.isChecked =
            file.selected

        holder.checkBox.setOnCheckedChangeListener {
                _, isChecked ->

            file.selected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

    private fun formatSize(size: Long): String {

        val kb = size / 1024.0
        val mb = kb / 1024.0

        return String.format(
            "%.2f MB",
            mb
        )
    }
}
