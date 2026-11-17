package com.safestoragecleaner.scanner

import java.io.File

class EmptyFolderScanner {

    fun scan(
        root: File
    ): MutableList<File> {

        val result =
            mutableListOf<File>()

        try {

            root.walkTopDown().forEach {

                if (
                    it.isDirectory &&
                    it.listFiles()?.isEmpty() == true
                ) {
                    result.add(it)
                }
            }

        } catch (_: Exception) {
        }

        return result
    }
}
