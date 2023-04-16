package commands

import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command

class Ping : CommandStruct {
    override val command = Command("ping", "Ping me!") {
        slashFunction {
            reply(":bellhop: **Pong!**").queue()
        }
    }.build()
}