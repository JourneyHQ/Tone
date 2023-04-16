package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.controls.skip

class Skip : CommandStruct {
    override val command = Command("skip", "Skip the current track.") {
        filter(guildOnly = true)
        slashFunction {
            skip(guild!!) {
                reply(it).complete() // "Now playing" notification will be sent right after this.
            }
        }
    }.build()
}