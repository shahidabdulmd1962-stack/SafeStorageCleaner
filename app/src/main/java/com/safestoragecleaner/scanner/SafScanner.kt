package com.safestoragecleaner.scanner

import androidx.documentfile.provider.DocumentFile

class SafScanner {

    private val junkExtensions = listOf(
        ".tmp",
        ".temp",
        ".log",
        ".bak",
        ".cache",
        ".old"
    )

    fun scan(
        root: DocumentFile
    ): MutableList<DocumentFile> {

        val result =
            mutableListOf<DocumentFile>()

        scanRecursive(
            root,
            result
        )

        return result
    }

    private fun scanRecursive(
        current: DocumentFile,
        result: MutableList<DocumentFile>
    ) {

        current.listFiles().forEach {

            if (it.isDirectory) {

                scanRecursive(
                    it,
                    result
                )

            } else {

                val name =
                    it.name?.lowercase() ?: ""

                if (
                    junkExtensions.any {
                        name.endsWith(it)
                    }
                ) {
                    result.add(it)
                }
            }
        }
    }
}
