package com.safestoragecleaner.scanner

import java.io.File

class ApkScanner {

    fun scan(
        root: File
    ): MutableList<File> {

        val result =
            mutableListOf<File>()

        try {

            root.walkTopDown().forEach {

                if (
                    it.isFile &&
                    it.name.lowercase()
                        .endsWith(".apk")
                ) {

                    result.add(it)
                }
            }

        } catch (_: Exception) {
        }

        return result
    }
}
