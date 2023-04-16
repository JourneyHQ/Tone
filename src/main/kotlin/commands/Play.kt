package commands

import dev.minn.jda.ktx.events.onEntitySelect
import dev.minn.jda.ktx.interactions.components.EntitySelectMenu
import dev.minn.jda.ktx.interactions.components.getOption
import dev.minn.jda.ktx.interactions.components.row
import dev.yuua.journeylib.qnortz.functions.command.CommandStruct
import dev.yuua.journeylib.qnortz.functions.command.builder.Command
import music.connectAudio
import music.play
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget

class Play : CommandStruct {
    override val command = Command("play", "Play music!") {
        filter(guildOnly = true)
        option<String>(
            "term",
            "Enter keywords to search on YouTube, or enter url to load music directly.",
            required = true
        )
        slashFunction {
            val term = getOption<String>("term")!!.let {
                if (it.startsWith("https://") || it.startsWith("http://")) it
                else "ytsearch:$it"
            }
            val selfAudioChannel = guild!!.selfMember.voiceState!!.channel
            val memberAudioChannel = member!!.voiceState!!.channel

            // If the bot is already on a voice channel.
            if (selfAudioChannel != null) {
                play(guild!!, channel, member!!, null, term) { messageCreateData ->
                    reply(messageCreateData).queue()
                }
                return@slashFunction
            }

            if (memberAudioChannel == null) {
                jda.onEntitySelect(interaction.id) {
                    val audioChannel = it.mentions.channels.first() as AudioChannel
                    guild!!.connectAudio(audioChannel)
                    it.reply(":white_check_mark: **Joined to** ${audioChannel.asMention}").queue()

                    play(guild!!, channel, member!!, null, term) { messageCreateData ->
                        it.hook.sendMessage(messageCreateData).queue()
                    }
                }

                replyComponents(
                    row(EntitySelectMenu(interaction.id, listOf(SelectTarget.CHANNEL), "Select channel to join...") {
                        setChannelTypes(ChannelType.VOICE, ChannelType.STAGE)
                    })
                ).queue()
            } else {
                guild!!.connectAudio(memberAudioChannel)
                reply(":white_check_mark: **Joined to** ${memberAudioChannel.asMention}").queue()

                play(guild!!, channel, member!!, null, term) { messageCreateData ->
                    hook.sendMessage(messageCreateData).queue()
                }
            }
        }
    }.build()
}