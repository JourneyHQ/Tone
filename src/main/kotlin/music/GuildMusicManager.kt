package music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager


class GuildMusicManager(manager: AudioPlayerManager) {
    val player = manager.createPlayer()
    val scheduler = TrackScheduler(player)

    fun getSendHandler() = AudioPlayerSendHandler(player)

    init {
        player.addListener(scheduler)
    }
}