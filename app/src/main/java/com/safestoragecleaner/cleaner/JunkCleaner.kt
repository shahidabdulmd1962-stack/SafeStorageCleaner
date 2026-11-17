package com.safestoragecleaner.cleaner

import java.io.File

class JunkCleaner {

    private val protectedExtensions = listOf(
        ".jpg",
        ".jpeg",
        ".png",
        ".gif",
        ".mp4",
        ".mkv",
        ".avi",
        ".mp3",
        ".wav",
        ".aac",
        ".pdf",
        ".doc",
        ".docx"
    )

    fun deleteFile(path: String): Boolean {

        val file = File(path)

        if (!file.exists()) {
            return false
        }

        val name =
            file.name.lowercase()

        if (
            protectedExtensions.any {
                name.endsWith(it)
            }
        ) {
            return false
        }

        return try {
            file.delete()
        } catch (_: Exception) {
            false
        }
    }
}
