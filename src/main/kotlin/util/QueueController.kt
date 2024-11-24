package util

import entity.creep.Creep

interface QueueController {
	val creepQueueCount: List<Int>
	val currentCreep: Creep?
	val spawnProgress: Float

	fun queueCreep(creep: Creep): Boolean
	fun processQueue(delta: Double) {}
}