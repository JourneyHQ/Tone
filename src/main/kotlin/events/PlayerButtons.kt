package events

import dev.minn.jda.ktx.events.onButton
import dev.minn.jda.ktx.events.onStringSelect
import dev.yuua.journeylib.qnortz.functions.event.EventStruct
import music.controls.pause
import music.controls.previous
import music.play
import music.controls.skip
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.messages.MessageEditData

class PlayerButtons : EventStruct {
    override val script: JDA.() -> Unit = {
        onStringSelect("playnow") { event ->
            if (!event.isFromGuild) return@onStringSelect

            play(
                event.guild!!,
                event.channel,
                event.member!!,
                event.message,
                event.selectedOptions.first().value,
                false
            ) {
                event.editMessage(MessageEditData.fromCreateData(it)).queue()
            }
        }

        onButton("skip") { event ->
            if (!event.isFromGuild) return@onButton

            skip(event.guild!!) {
                event.reply(it).complete() // "Now playing" notification will be sent right after this.
            }
        }

        onButton("pause") { event ->
            if (!event.isFromGuild) return@onButton

            pause(event.guild!!) {
                event.reply(it).queue()
            }
        }

        onButton("previous") { event ->
            if (!event.isFromGuild) return@onButton

            previous(event.guild!!) {
                event.reply(it).complete() // "Now playing" notification will be sent right after this.
            }
        }
    }
}