package com.safestoragecleaner.model

import androidx.documentfile.provider.DocumentFile

data class SafFileItem(

    val file: DocumentFile,

    var selected: Boolean = false
)
