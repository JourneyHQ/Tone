package music.controls

import dev.minn.jda.ktx.messages.MessageCreate
import music.PlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.utils.messages.MessageCreateData

fun pause(guild: Guild, replier: (MessageCreateData) -> Unit) {
    val guildMusicManager = PlayerManager.instance!!.getGuildMusicManager(guild)
    val player = guildMusicManager.player

    if (player.playingTrack == null) {
        replier(MessageCreate {
            content = ":question: **Could not pause music.** No music is playing now."
        })
        return
    }

    player.isPaused = !player.isPaused

    replier(MessageCreate {
        content = if (player.isPaused) ":pause_button: **Music paused.**" else ":arrow_forward: **Music resumed.**"
    })
}