package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.controls.pause
import music.controls.skip

class Resume : CommandStruct {
    override val command = Command("resume", "Resume or pause the current track. Same as '/pause'.") {
        filter(guildOnly = true)
        slashFunction {
            pause(guild!!) {
                reply(it).queue()
            }
        }
    }.build()
}