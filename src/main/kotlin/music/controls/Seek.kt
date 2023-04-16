package music.controls

import dev.minn.jda.ktx.messages.MessageCreate
import music.PlayerManager
import music.embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

fun seek(seconds: Int, guild: Guild, replier: (MessageCreateData) -> Unit) {
    val guildMusicManager = PlayerManager.instance!!.getGuildMusicManager(guild)
    val track = guildMusicManager.player.playingTrack

    if (track == null) {
        replier(MessageCreate {
            content = ":question: **No music is playing now.**"
        })
        return
    }

    if (!track.isSeekable) {
        replier(MessageCreate {
            content = ":question: **Could not skip ${seconds}s.** Maybe the music is a live stream."
        })
        return
    }

    var seekedPosition = track.position + seconds * 1000

    if (track.duration <= seekedPosition) {
        skip(guild, replier)
        return
    }

    if (seekedPosition < 0) seekedPosition = 0

    // 0 <= seeked position < duration

    track.position = seekedPosition

    val duration = track.duration

    fun Long.formatElapsedTime() = this.milliseconds.toComponents { hours, minutes, seconds, _ ->
        if (hours == 0L) "${minutes}:${seconds}"
        else "${hours}:${minutes}:${seconds}"
    }

    val elapsedTimeCount = ((seekedPosition.toFloat() / duration.toFloat()) * 14f).roundToInt()
    val positionBar = "=".repeat(elapsedTimeCount) + ">" + "-".repeat(14 - elapsedTimeCount)

    val timeElapsed = seekedPosition.formatElapsedTime()
    val timeRemaining = (duration - seekedPosition).formatElapsedTime()

    replier(MessageCreate {
        content = ":hourglass: **Music skipped ${seconds}s** $timeElapsed `$positionBar` -$timeRemaining"
        embeds += track.embed()
    })
}
