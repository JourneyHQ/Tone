package music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack

enum class Loop {
    Disabled, Single, All
}

class TrackList {
    var list = mutableListOf<AudioTrack>()
    var cursor = -1

    var loop = Loop.Disabled
    var loopTrack: Int? = null

    fun add(track: AudioTrack) {
        list.add(track)
    }

    fun insert(track: AudioTrack) {
        list.add(cursor + 1, track)
    }

    fun remove(index: Int): AudioTrack? {
        if (index !in cursor + 1 until list.size) return null

        return list.removeAt(index)
    }

    fun next() = moveCursor(1)

    fun previous() = moveCursor(-1)

    // fixme
    fun moveCursor(step: Int): AudioTrack? {
        val steppedCursor = cursor + step
        return when (loop) {
            Loop.Disabled -> when (steppedCursor) {
                in (0 until list.size) -> {
                    cursor = steppedCursor
                    list[cursor]
                }

                -1, list.size -> {
                    cursor = steppedCursor
                    null
                }

                else -> null
            }

            Loop.Single -> list[loopTrack!!]

            Loop.All -> {
                cursor = if (steppedCursor in (loopTrack!! until list.size)) {
                    steppedCursor
                } else {
                    val loopStep = (steppedCursor - loopTrack!! + 1) % (list.size - loopTrack!!)
                    if (loopStep == 0) list.size - 1 else loopStep - 1 + loopTrack!!
                }
                list[cursor]
            }
        }
    }

    fun loop(loopType: Loop): AudioTrack? {
        if (cursor == -1 || cursor == list.size) return null

        loop = loopType
        loopTrack = cursor

        return list[loopTrack!!]
    }

    data class Queue(val current: AudioTrack?, val list: List<AudioTrack>)

    fun queue(): Queue = when (cursor) {
        -1 -> Queue(null, list.subList(0, list.size))
        list.size -> Queue(null, emptyList())
        else -> Queue(list[cursor], list.subList(cursor + 1, list.size))
    }

    fun history() = when (cursor) {
        -1 -> Queue(null, emptyList())
        list.size -> Queue(null, list.subList(0, cursor))
        else -> Queue(list[cursor], list.subList(0, cursor))
    }

    fun shuffle() {
        list = (history().list + listOf(list[cursor]) + queue().list.shuffled()).toMutableList()
    }
}