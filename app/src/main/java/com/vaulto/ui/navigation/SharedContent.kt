package com.vaulto.ui.navigation

import android.content.Intent
import android.net.Uri

data class SharedContent(val title: String, val url: String, val note: String, val id: Long = System.nanoTime()) {
    companion object {
        fun fromIntent(intent: Intent?): SharedContent? {
            if (intent?.action != Intent.ACTION_SEND || !intent.type.orEmpty().startsWith("text/")) return null
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)?.trim().orEmpty()
            val url = Regex("https?://[^\\s]+", RegexOption.IGNORE_CASE).find(text)?.value.orEmpty()
            val suppliedTitle = intent.getStringExtra(Intent.EXTRA_TITLE)?.trim().orEmpty()
            val nonUrlLine = text.lineSequence().map { it.trim() }.firstOrNull { it.isNotBlank() && it != url }.orEmpty()
            val hostTitle = runCatching { Uri.parse(url).host?.removePrefix("www.")?.replaceFirstChar { it.uppercase() } }.getOrNull().orEmpty()
            val title = suppliedTitle.ifBlank { nonUrlLine }.ifBlank { hostTitle }.ifBlank { "Shared item" }
            val note = text.replace(url, "").trim().takeIf { it != title }.orEmpty()
            return SharedContent(title, url, note)
        }
    }
}
