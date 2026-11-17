package com.safestoragecleaner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safestoragecleaner.adapter.FileAdapter
import com.safestoragecleaner.cleaner.CacheCleaner
import com.safestoragecleaner.cleaner.JunkCleaner
import com.safestoragecleaner.cleaner.SafCleaner
import com.safestoragecleaner.model.FileItem
import com.safestoragecleaner.scanner.JunkScanner
import com.safestoragecleaner.scanner.SafScanner

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter

    private lateinit var btnPickFolder: Button
    private lateinit var btnScan: Button
    private lateinit var btnDelete: Button

    private lateinit var txtTotalSize: TextView

    private val fileList = mutableListOf<FileItem>()

    private val safFiles =
        mutableListOf<DocumentFile>()

    private var selectedFolderUri: Uri? = null

    private var safMode = false

    private val PICK_FOLDER = 200

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnPickFolder =
            findViewById(R.id.btnPickFolder)

        btnScan =
            findViewById(R.id.btnScan)

        btnDelete =
            findViewById(R.id.btnDelete)

        txtTotalSize =
            findViewById(R.id.txtTotalSize)

        recyclerView =
            findViewById(R.id.recyclerFiles)

        adapter =
            FileAdapter(fileList)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter =
            adapter

        checkPermission()

        btnPickFolder.setOnClickListener {

            val intent =
                Intent(
                    Intent.ACTION_OPEN_DOCUMENT_TREE
                )

            startActivityForResult(
                intent,
                PICK_FOLDER
            )
        }

        btnScan.setOnClickListener {

            startScan()
        }

        btnDelete.setOnClickListener {

            val cacheCleaner =
                CacheCleaner()

            cacheCleaner.clearAppCache(this)

            var deletedCount = 0

            if (safMode) {

                val cleaner =
                    SafCleaner()

                safFiles.forEach {

                    val selected =
                        fileList.find { item ->
                            item.path ==
                                    it.uri.toString() &&
                                    item.selected
                        }

                    if (selected != null) {

                        if (
                            cleaner.deleteFile(it)
                        ) {
                            deletedCount++
                        }
                    }
                }

            } else {

                val cleaner =
                    JunkCleaner()

                fileList.forEach {

                    if (it.selected) {

                        if (
                            cleaner.deleteFile(
                                it.path
                            )
                        ) {
                            deletedCount++
                        }
                    }
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

        if (
            safMode &&
            selectedFolderUri != null
        ) {

            safFiles.clear()

            val root =
                DocumentFile.fromTreeUri(
                    this,
                    selectedFolderUri!!
                )

            if (root != null) {

                val scanner =
                    SafScanner()

                val files =
                    scanner.scan(root)

                safFiles.addAll(files)

                files.forEach {

                    fileList.add(
                        FileItem(
                            name =
                                it.name ?: "Unknown",
                            path =
                                it.uri.toString(),
                            size = 0
                        )
                    )
                }
            }

        } else {

            val scanner =
                JunkScanner()

            val files =
                scanner.scanStorage(
                    android.os.Environment
                        .getExternalStorageDirectory()
                )

            fileList.addAll(files)
        }

        adapter.notifyDataSetChanged()

        txtTotalSize.text =
            "Found ${fileList.size} junk files"
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == PICK_FOLDER &&
            data != null
        ) {

            selectedFolderUri =
                data.data

            safMode = true

            contentResolver
                .takePersistableUriPermission(
                    selectedFolderUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

            Toast.makeText(
                this,
                "Folder Selected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
