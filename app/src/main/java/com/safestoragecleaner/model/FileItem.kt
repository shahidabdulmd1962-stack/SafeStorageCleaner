package com.safestoragecleaner.model

data class FileItem(
    val name: String,
    val path: String,
    val size: Long,
    var selected: Boolean = false
)
