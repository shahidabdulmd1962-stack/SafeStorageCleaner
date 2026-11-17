package com.safestoragecleaner.scanner

import com.safestoragecleaner.model.FileItem
import java.io.File

class JunkScanner {

    private val junkExtensions = listOf(
    ".tmp",
    ".temp",
    ".log",
    ".bak",
    ".old",
    ".cache"
)

    fun scanStorage(
        root: File
    ): MutableList<FileItem> {

        val result =
            mutableListOf<FileItem>()

        try {

            root.walkTopDown().forEach { file ->

                if (
                    file.isFile &&
                    isJunkFile(file)
                ) {

                    result.add(
                        FileItem(
                            name = file.name,
                            path = file.absolutePath,
                            size = file.length()
                        )
                    )
                }
            }

        } catch (_: Exception) {
        }

        return result
    }

    private fun isJunkFile(
        file: File
    ): Boolean {

        val name =
            file.name.lowercase()

        return junkExtensions.any {
            name.endsWith(it)
        }
    }
}
