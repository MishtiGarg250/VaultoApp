package com.vaulto.ui.save

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaulto.data.entity.SavedItem
import com.vaulto.data.entity.Collection
import com.vaulto.data.repository.VaultoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.text.Html
import java.net.HttpURLConnection
import java.net.URL

class SaveViewModel(
    private val repository: VaultoRepository
) : ViewModel() {

    val collections = repository.collections

    fun save(
        title: String,
        url: String,
        note: String,
        collectionId: Long?
    ) {

        if (title.isBlank()) return

        viewModelScope.launch {

            val item = SavedItem(
                title = title,
                url = url.ifBlank { null },
                note = note.ifBlank { null },
                collectionId = collectionId
            )

            repository.saveItem(item)
        }
    }

    fun createCollection(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createCollection(Collection(name = name.trim())) }
    }

    /** Gets the page's public Open Graph title, with the HTML title as a fallback. */
    fun loadSharedTitle(url: String, onTitleReady: (String) -> Unit) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) return
        viewModelScope.launch {
            val pageTitle = withContext(Dispatchers.IO) {
                runCatching {
                    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                        instanceFollowRedirects = true
                        connectTimeout = 8_000
                        readTimeout = 8_000
                        setRequestProperty("User-Agent", "Mozilla/5.0 (Android) Vaulto/1.0")
                    }
                    connection.inputStream.bufferedReader().use { reader ->
                        val html = reader.readText().take(500_000)
                        connection.disconnect()
                        extractHtmlTitle(html)
                    }
                }.getOrNull()
            }
            if (!pageTitle.isNullOrBlank()) onTitleReady(pageTitle)
        }
    }

    private fun extractHtmlTitle(html: String): String? {
        val ogTitle = Regex(
            """<meta[^>]+(?:property|name)=[\"']og:title[\"'][^>]+content=[\"']([^\"']+)[\"']""",
            RegexOption.IGNORE_CASE
        ).find(html)?.groupValues?.getOrNull(1)
        val reversedOgTitle = Regex(
            """<meta[^>]+content=[\"']([^\"']+)[\"'][^>]+(?:property|name)=[\"']og:title[\"']""",
            RegexOption.IGNORE_CASE
        ).find(html)?.groupValues?.getOrNull(1)
        val title = ogTitle ?: reversedOgTitle ?: Regex(
            """<title[^>]*>(.*?)</title>""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        ).find(html)?.groupValues?.getOrNull(1)
        return title?.let { Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString().trim() }?.takeIf { it.isNotBlank() }
    }
}
