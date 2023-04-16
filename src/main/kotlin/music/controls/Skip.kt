package music.controls

import dev.minn.jda.ktx.messages.MessageCreate
import dev.yuua.journeylib.qnortz.QnortzColor
import music.PlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.utils.messages.MessageCreateData

fun skip(guild: Guild, replier: (MessageCreateData) -> Unit) {
    val guildMusicManager = PlayerManager.instance!!.getGuildMusicManager(guild)
    val scheduler = guildMusicManager.scheduler

    val previousTrack = guildMusicManager.player.playingTrack

    replier(MessageCreate {
        content = if (previousTrack != null) ":track_next: **Track skipped.**" else ":question: **No track skipped.**"
    })

    scheduler.nextTrack(withQueue = true)
}