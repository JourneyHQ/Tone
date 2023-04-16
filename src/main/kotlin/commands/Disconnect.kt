package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.PlayerManager

class Disconnect : CommandStruct {
    override val command = Command("disconnect", "Disconnects the bot from the voice channel.") {
        filter(guildOnly = true)
        slashFunction {
            val channel = guild!!.audioManager.connectedChannel

            if (channel == null) {
                reply(":question: **Not connected to the voice channel.**").queue()
            } else {
                PlayerManager.instance.getGuildMusicManager(guild!!).player.destroy()
                guild!!.audioManager.closeAudioConnection()
                reply(":wave: **Disconnected from** ${channel.asMention}").queue()
            }
        }
    }.build()
}