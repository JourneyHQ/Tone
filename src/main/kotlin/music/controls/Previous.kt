package music.controls

import dev.minn.jda.ktx.messages.MessageCreate
import music.PlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.utils.messages.MessageCreateData

fun previous(guild: Guild, replier: (MessageCreateData) -> Unit) {
    val guildMusicManager = PlayerManager.instance!!.getGuildMusicManager(guild)
    val scheduler = guildMusicManager.scheduler

    val currentTrack = guildMusicManager.player.playingTrack

    val history = scheduler.trackList.history().list
    val historyIsEmpty = history.isEmpty()

    // if 10 or fewer seconds have passed since the music started.
    if (currentTrack != null && currentTrack.position >= 10000) {
        currentTrack.position = 0

        replier(MessageCreate {
            content = ":zero: **${currentTrack.info.title} will be played again from the beginning.**" +
                    if (!historyIsEmpty) "\nTo back to the previous track, execute `/previous` again in 10 seconds" else ""
        })
        return
    }

    if (historyIsEmpty) {
        replier(MessageCreate {
            content = ":question: **Could not back to the previous track.** No music was played previously."
        })
        return
    }

    replier(MessageCreate {
        content = ":leftwards_arrow_with_hook: **Got back to the previous track.**"
    })

    scheduler.previousTrack(withQueue = true)
}