import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    @SerialName("discord") val discordToken: String,
    @SerialName("discord-dev") val discordTokenDev: String,
    @SerialName("youtube") val youTubeToken: String,
    @SerialName("youtube-dev") val youTubeTokenDev: String
)
