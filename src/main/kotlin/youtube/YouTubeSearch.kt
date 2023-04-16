package youtube

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.minn.jda.ktx.messages.Embed
import dev.yuua.journeylib.qnortz.QnortzColor
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.Member

@Serializable
data class YouTubeSearch(
    val kind: String,
    val etag: String,
    val nextPageToken: String? = null,
    val regionCode: String,
    val pageInfo: YouTubePageInfo,
    val items: List<YouTubeItem>
)

@Serializable
data class YouTubePageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

@Serializable
data class YouTubeItem(
    val kind: String,
    val etag: String,
    val id: YouTubeID,
    val snippet: YouTubeSnippet
) {
    val url = "https://www.youtube.com/watch?v=${this.id.videoId}"
    val channelUrl = "https://www.youtube.com/channel/${snippet.channelId}"

    fun embed(
        member: Member,
        history: List<AudioTrack> = emptyList(),
        queue: List<AudioTrack> = emptyList()
    ) = Embed {
        author {
            name = snippet.channelTitle
            url = channelUrl
        }
        title = snippet.title
        description = snippet.description
        thumbnail = snippet.thumbnails.default.url
        color = QnortzColor.Pink.int()
        footer {
            iconUrl = member.user.avatarUrl
            name = member.user.asTag
        }

        if (history.isNotEmpty()) {
            field {
                name = ":clock3: History"
                value = "\"${history.last().info.title}\"" +
                        if (history.size > 1) "and ${history.size - 1} more." else ""
                inline = true
            }
        }

        if (queue.isNotEmpty()) {
            field {
                name = ":arrow_right: Play next"
                value = "\"${queue.first().info.title}\"" +
                        if (queue.size > 1) "and ${queue.size - 1} more." else ""
            }
        }
    }
}

@Serializable
data class YouTubeID(
    val kind: String,
    val videoId: String
)

@Serializable
data class YouTubeSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: YouTubeThumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

@Serializable
data class YouTubeThumbnails(
    val default: YouTubeThumbnail,
    val medium: YouTubeThumbnail,
    val high: YouTubeThumbnail
)

@Serializable
data class YouTubeThumbnail(
    val url: String,
    val width: Int,
    val height: Int
)