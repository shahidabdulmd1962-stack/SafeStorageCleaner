package com.safestoragecleaner.cleaner

import android.content.Context

class CacheCleaner {

    fun clearAppCache(
        context: Context
    ): Boolean {

        return try {

            context.cacheDir.deleteRecursively()

            true

        } catch (_: Exception) {

            false
        }
    }
}
