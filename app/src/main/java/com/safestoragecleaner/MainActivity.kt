package com.safestoragecleaner

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safestoragecleaner.adapter.FileAdapter
import com.safestoragecleaner.model.FileItem

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: FileAdapter

    private lateinit var btnScan: Button

    private lateinit var btnDelete: Button

    private lateinit var txtTotalSize: TextView

    private val fileList =
        mutableListOf<FileItem>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        recyclerView =
            findViewById(R.id.recyclerFiles)

        btnScan =
            findViewById(R.id.btnScan)

        btnDelete =
            findViewById(R.id.btnDelete)

        txtTotalSize =
            findViewById(R.id.txtTotalSize)

        adapter =
            FileAdapter(fileList)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter =
            adapter

        btnScan.setOnClickListener {

            fileList.clear()

            fileList.add(
                FileItem(
                    "temp.log",
                    "/storage/temp.log",
                    1050000
                )
            )

            fileList.add(
                FileItem(
                    "cache.tmp",
                    "/storage/cache.tmp",
                    2097152
                )
            )

            adapter.notifyDataSetChanged()

            txtTotalSize.text =
                "Junk Size: 3 MB"
        }

        btnDelete.setOnClickListener {

            fileList.removeAll {
                it.selected
            }

            adapter.notifyDataSetChanged()
        }
    }
}
