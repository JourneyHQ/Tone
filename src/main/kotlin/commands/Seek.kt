package commands

import dev.minn.jda.ktx.interactions.components.getOption
import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.controls.seek

class Seek : CommandStruct {
    override val command = Command("seek", "Skip the specified number of seconds of music.") {
        filter(guildOnly = true)
        option<Int>("seconds", "The number of seconds to skip.", required = true)
        slashFunction {
            seek(getOption<Int>("seconds")!!, guild!!) {
                reply(it).queue()
            }
        }
    }.build()
}