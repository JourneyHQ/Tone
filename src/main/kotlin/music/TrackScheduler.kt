package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.minn.jda.ktx.messages.MessageCreate

class TrackScheduler(private val player: AudioPlayer) : AudioEventAdapter() {
    val trackList = TrackList()

    fun queue(track: AudioTrack, noInterrupt: Boolean = true) {
        if (player.startTrack(track, noInterrupt)) {
            trackList.insert(track)
            trackList.next()
        } else trackList.add(track)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (!endReason.mayStartNext) return
        nextTrack()
    }

    fun nextTrack(noInterrupt: Boolean = false, withQueue: Boolean = false) =
        playTrack(trackList.next(), noInterrupt, withQueue)

    fun previousTrack(noInterrupt: Boolean = false, withQueue: Boolean = false) =
        playTrack(trackList.previous(), noInterrupt, withQueue)

    private fun playTrack(track: AudioTrack?, noInterrupt: Boolean = false, withQueue: Boolean = false): AudioTrack? {
        val started = try {
            player.startTrack(track, noInterrupt)
        } catch (e: IllegalStateException) {
            player.startTrack(track?.makeClone(), noInterrupt)
        }

        if (started) {
            val trackInfo = track?.trackInfo()
            val youtubeItem = trackInfo?.youtube
            val history = trackList.history().list
            val queue = trackList.queue().list

            trackInfo?.channel?.sendMessage(MessageCreate {
                content = ":musical_note: **Now Playing!**"
                embeds += if (withQueue) {
                    youtubeItem!!.embed(trackInfo.member, history, queue)
                } else {
                    youtubeItem!!.embed(trackInfo.member)
                }
                components += MusicActionRows.controls
                components += MusicActionRows.youtubeLinks(youtubeItem!!.url, youtubeItem.channelUrl)
            })?.queue()
        }

        return if (started) track else null
    }
}