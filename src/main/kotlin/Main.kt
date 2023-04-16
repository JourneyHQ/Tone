import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.light
import dev.yuua.journeylib.journal.Journal
import dev.yuua.journeylib.qnortz.Qnortz
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.requests.GatewayIntent
import youtube.YouTube
import kotlin.io.path.Path

class Main : CliktCommand() {
    private val configPath by option(
        "-c", "--config",
        help = "The config file location."
    ).path(mustExist = true, canBeDir = false).default(Path("./config.json"))

    private val devEnv by option(
        "-d", "--dev",
        help = "Whether to enable development environment."
    ).flag(default = false)

    private val discordToken by option(
        "-t", "--token",
        help = "The token for discord bot."
    )

    private val youTubeToken by option(
        "-y", "--youtube",
        help = "The token for YouTube Data API."
    )

    override fun run() {
        val journal = Journal("Tone")

        val configFile = configPath.toFile()
        val config = Json.decodeFromString(Config.serializer(), configFile.readText())

        runBlocking {
            Tone.qnortz = Qnortz(
                name = "Tone",
                token = discordToken ?: if (devEnv) config.discordTokenDev else config.discordToken,
                *GatewayIntent.values()
            ) {
                enableCommands("commands")
                enableEvents("events")
                if (devEnv) enableDevEnv("dev", "839462224505339954")
            }.build()
            Tone.youtube = YouTube(youTubeToken ?: if (devEnv) config.youTubeTokenDev else config.youTubeToken)
        }
    }
}

fun main(args: Array<String>) = Main().main(args)