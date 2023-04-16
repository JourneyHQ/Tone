package music

import dev.minn.jda.ktx.interactions.components.row
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button

object MusicActionRows {
    val controls = row(
        Button.secondary("previous", Emoji.fromUnicode("⏮️")),
        Button.secondary("pause", Emoji.fromUnicode("⏯️")),
        Button.secondary("skip", Emoji.fromUnicode("⏭️"))
    )

    fun youtubeLinks(videoURL: String, channelURL: String) = row(
        Button.link(videoURL, "Watch on YouTube"),
        Button.link(channelURL, "View this channel on YouTube")
    )
}
