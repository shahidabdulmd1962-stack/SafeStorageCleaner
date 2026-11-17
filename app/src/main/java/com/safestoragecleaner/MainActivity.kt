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
import androidx.documentfile.provider.DocumentFile
import com.safestoragecleaner.scanner.SafScanner
import android.provider.DocumentsContract
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private lateinit var btnPickFolder: Button

private var selectedFolderUri: Uri? = null

private val PICK_FOLDER = 200
    private lateinit var btnScan: Button
    private lateinit var btnDelete: Button
    private lateinit var txtTotalSize: TextView

    private val fileList =
        mutableListOf<FileItem>()
        private var safMode = false

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)
btnPickFolder =
    findViewById(R.id.btnPickFolder)
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
btnPickFolder.setOnClickListener {

    val intent =
        Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

    startActivityForResult(
        intent,
        PICK_FOLDER
    )
}
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

  private fun startScan() {

    fileList.clear()

    if (
        safMode &&
        selectedFolderUri != null
    ) {

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

        contentResolver.takePersistableUriPermission(
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
