package youtube

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json


class YouTube(private val key: String) {
    private fun baseUrl(term: String, limit: Int) =
        "https://www.googleapis.com/youtube/v3/search?type=video&part=snippet&q=$term&key=$key&maxResults=$limit"

    suspend fun search(term: String, limit: Int): YouTubeSearch {
        val httpClient = HttpClient(CIO)
        val response = httpClient.get(baseUrl(term, limit))

        return Json.decodeFromString(YouTubeSearch.serializer(), response.bodyAsText())
    }
}