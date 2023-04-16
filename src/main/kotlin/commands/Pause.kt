package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.controls.pause
import music.controls.skip

class Pause : CommandStruct {
    override val command = Command("pause", "Pause or resume the current track. Same as '/resume'.") {
        filter(guildOnly = true)
        slashFunction {
            pause(guild!!) {
                reply(it).queue()
            }
        }
    }.build()
}