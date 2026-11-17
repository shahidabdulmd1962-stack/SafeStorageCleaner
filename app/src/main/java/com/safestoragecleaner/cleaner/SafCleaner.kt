package com.safestoragecleaner.cleaner

import androidx.documentfile.provider.DocumentFile

class SafCleaner {

    fun deleteFile(
        file: DocumentFile
    ): Boolean {

        return try {

            file.delete()

        } catch (_: Exception) {

            false
        }
    }
}
