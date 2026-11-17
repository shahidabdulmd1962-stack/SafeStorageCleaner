package com.safestoragecleaner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safestoragecleaner.adapter.FileAdapter
import com.safestoragecleaner.model.FileItem
import com.safestoragecleaner.scanner.JunkScanner
import java.io.File
import android.widget.Toast
import com.safestoragecleaner.cleaner.JunkCleaner
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
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

        checkPermission()

        btnScan.setOnClickListener {
btnDelete.setOnClickListener {

    val cleaner =
        JunkCleaner()

    val selectedFiles =
        fileList.filter {
            it.selected
        }

    var deletedCount = 0

    selectedFiles.forEach {

        if (
            cleaner.deleteFile(
                it.path
            )
        ) {
            deletedCount++
        }
    }

    fileList.removeAll {
        it.selected
    }

    adapter.notifyDataSetChanged()

    Toast.makeText(
        this,
        "$deletedCount file(s) deleted",
        Toast.LENGTH_LONG
    ).show()
}
            startScan()
        }
    }

    private fun checkPermission() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                100
            )
        }
    }

    private fun startScan() {

        fileList.clear()

        val root: File =
            Environment.getExternalStorageDirectory()

        val scanner =
            JunkScanner()

        val result =
            scanner.scanStorage(root)

        fileList.addAll(result)

        adapter.notifyDataSetChanged()

        val totalBytes =
            fileList.sumOf {
                it.size
            }

        txtTotalSize.text =
            "Junk Size: ${
                formatSize(totalBytes)
            }"
    }

    private fun formatSize(
        bytes: Long
    ): String {

        val mb =
            bytes / 1024.0 / 1024.0

        return String.format(
            "%.2f MB",
            mb
        )
    }
}
