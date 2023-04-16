package music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import youtube.YouTubeItem
import java.util.*

data class TrackInfo(
    val uuid: UUID,
    val youtube: YouTubeItem,
    val member: Member,
    val channel: MessageChannelUnion,
    val message: Message?,
    val suppressNowPlaying: Boolean
)

fun AudioTrack.trackInfo() = this.userData as TrackInfo

fun AudioTrack.embed() = this.trackInfo().youtube.embed(this.trackInfo().member)

fun AudioTrack.urlButtons() =
    MusicActionRows.youtubeLinks(this.trackInfo().youtube.url, this.trackInfo().youtube.channelUrl)