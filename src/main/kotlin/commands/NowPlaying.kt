package commands

import dev.minn.jda.ktx.messages.MessageCreate
import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.MusicActionRows
import music.PlayerManager
import music.embed
import music.urlButtons

class NowPlaying : CommandStruct {
    override val command = Command("nowplaying", "Show the current playing track.") {
        filter(guildOnly = true)
        slashFunction {
            val track = PlayerManager.instance!!.getGuildMusicManager(guild!!).player.playingTrack
            if (track == null) {
                reply(":question: **No track is playing.**").queue()
            } else {
                reply(MessageCreate {
                    embeds += track.embed()
                    components += MusicActionRows.controls
                    components += track.urlButtons()
                }).queue()
            }
        }
    }.build()
}