package util

import entity.creep.Creep
import log

class QueueControllerImpl(
	private val spawnBusy: () -> Boolean,
	private val creepReady: (Creep) -> Unit
) : QueueController {
	private val creepQueue = CreepQueue()
	override val creepQueueCount: List<Int>
		get() = creepQueue.count

	override var spawnProgress: Float = 0f
	private var buildProgressTime: Float = 0f

	override var currentCreep: Creep? = null

	override fun queueCreep(creep: Creep): Boolean {
		val canPush = creepQueue.push(creep)
		log.d("queueCreep(${creep.type}) canPush=$canPush")
		currentCreep = creepQueue.peek()
		return canPush
	}

	override fun processQueue(delta: Double) {
		val buildTime = buildTimeInSeconds(currentCreep?.buildTime ?: 1f)

		spawnProgress = buildProgressTime / buildTime

		if (currentCreep != null) {
			buildProgressTime += delta.toFloat()
		}
		val spawnIsBusy = spawnBusy()
		if (buildProgressTime > buildTime && spawnIsBusy)
			buildProgressTime = buildTime
		if (buildProgressTime >= buildTime && !spawnIsBusy) {
			val newCreep = creepQueue.pop()!!
			creepReady(newCreep)
			buildProgressTime = 0f
			spawnProgress = 0f
			currentCreep = creepQueue.peek()
		}
	}

	private fun buildTimeInSeconds(buildTime: Float): Float {
		return 1 / buildTime * 0.75f
	}
}