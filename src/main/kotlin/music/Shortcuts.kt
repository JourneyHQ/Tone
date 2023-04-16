package music

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.utils.messages.MessageCreateData

fun Guild.connectAudio(channel: AudioChannel) =
    this.audioManager.openAudioConnection(channel)

fun play(
    guild: Guild,
    channel: MessageChannelUnion,
    member: Member,
    message: Message?,
    term: String,
    noInterrupt: Boolean = true,
    replier: (MessageCreateData) -> Unit
) = PlayerManager.instance!!.loadAndPlay(guild, channel, member, message, term, noInterrupt, replier)