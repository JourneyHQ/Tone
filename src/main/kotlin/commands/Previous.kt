package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.controls.previous
import music.controls.skip

class Previous : CommandStruct {
    override val command = Command("previous", "Go back to the previous track.") {
        filter(guildOnly = true)
        slashFunction {
            previous(guild!!) {
                reply(it).complete() // "Now playing" notification will be sent right after this.
            }
        }
    }.build()
}