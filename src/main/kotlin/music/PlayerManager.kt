package music

import Tone
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.minn.jda.ktx.interactions.components.StringSelectMenu
import dev.minn.jda.ktx.interactions.components.option
import dev.minn.jda.ktx.interactions.components.row
import dev.minn.jda.ktx.messages.MessageCreate
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import java.util.*


class PlayerManager {
    private val playerManager = DefaultAudioPlayerManager()
    private val musicManagers = hashMapOf<Long, GuildMusicManager>()

    companion object {
        @get:Synchronized
        var instance: PlayerManager = PlayerManager()
    }

    @Synchronized
    fun getGuildMusicManager(guild: Guild): GuildMusicManager {
        val guildID: Long = guild.idLong
        var musicManager = musicManagers[guildID]
        if (musicManager == null) {
            musicManager = GuildMusicManager(playerManager)
            musicManagers[guildID] = musicManager
        }
        guild.audioManager.sendingHandler = musicManager.getSendHandler()
        return musicManager
    }

    fun loadAndPlay(
        guild: Guild,
        channel: MessageChannelUnion,
        member: Member,
        message: Message?,
        term: String,
        noInterrupt: Boolean = true,
        replier: (MessageCreateData) -> Unit
    ) {
        val musicManager = getGuildMusicManager(guild)
        playerManager.loadItemOrdered(musicManager, term, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                val youTubeItem = runBlocking {
                    Tone.youtube.search(track.info.identifier, 1)
                }.items.first()

                val willPlayImmediately = musicManager.player.playingTrack == null || !noInterrupt

                track.userData = TrackInfo(
                    UUID.randomUUID(),
                    youTubeItem,
                    member,
                    channel,
                    message,
                    willPlayImmediately
                )

                val replyMessage = MessageCreate {
                    content =
                        if (willPlayImmediately) ":musical_note: **Now Playing!**" else ":card_box: **Added to the queue!**"
                    embeds += youTubeItem.embed(member)
                    components += MusicActionRows.controls
                    components += MusicActionRows.youtubeLinks(youTubeItem.url, youTubeItem.channelUrl)
                }

                musicManager.scheduler.queue(track, noInterrupt)

                replier(replyMessage)
            }

            //todo
            override fun playlistLoaded(playlist: AudioPlaylist) {
                if (playlist.isSearchResult) {
                    val tracks = playlist.tracks.let {
                        if (it.size < 26)
                            it.subList(1, it.size - 1)
                        else it.subList(1, 25)
                    }

                    val firstTrack = tracks.first()

                    val youTubeItem = runBlocking {
                        Tone.youtube.search(firstTrack.info.identifier, 1)
                    }.items.first()

                    val willPlayImmediately = musicManager.player.playingTrack == null || !noInterrupt

                    firstTrack.userData = TrackInfo(
                        UUID.randomUUID(),
                        youTubeItem,
                        member,
                        channel,
                        message,
                        willPlayImmediately
                    )

                    val trackInfo = firstTrack.trackInfo()

                    val replyMessage = MessageCreate {
                        content =
                            if (trackInfo.suppressNowPlaying) ":musical_note: **Now Playing!**" else ":card_box: **Added to the queue!**"
                        embeds += youTubeItem.embed(member)

                        components += MusicActionRows.controls
                        components += MusicActionRows.youtubeLinks(youTubeItem.url, youTubeItem.channelUrl)

                        components += row(
                            StringSelectMenu("playnow", "Not this? Check out other results we found.") {
                                tracks.forEach {
                                    option(
                                        it.info.title,
                                        it.info.uri,
                                        "by ${it.info.author}",
                                        Emoji.fromUnicode("🎵")
                                    )
                                }
                            }
                        )
                    }

                    musicManager.scheduler.queue(firstTrack, noInterrupt)

                    replier(replyMessage)
                }
            }

            override fun noMatches() {
                replier(MessageCreate {
                    content = ":question: **No music found!**"
                })
            }

            override fun loadFailed(e: FriendlyException) {
                replier(MessageCreate {
                    content = """
                        :interrobang: **Failed to load music!**
                        ```
                        ${e.message}
                        ```
                    """.trimIndent()
                })
            }
        })
    }

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }
}