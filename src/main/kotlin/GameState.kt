import kotlin.math.min

private val nextEpochExp = intArrayOf(1200, 8000, 40000, 80000, 200000, 200000)

class GameState {

    var money: Int = 75
    var experience: Int = 0
    private var _epoch: Int = 1
    val epoch: Int
        get() = _epoch

    val experienceProgress: Progress
        get() = min(1f, experience.toFloat() / nextEpochExp[epoch - 1])

    val canChangeEpoch: Boolean
        get() = epoch < nextEpochExp.size && experience >= nextEpochExp[epoch - 1]

    fun nextEpoch(): Boolean {
        return if (epoch < nextEpochExp.size) {
            if (experience >= nextEpochExp[epoch - 1]) {
                _epoch++
                true
            } else
                false
        } else
            false
    }
}