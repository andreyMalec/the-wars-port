package util

import entity.Creep

interface QueueController {
    val creepQueueCount: List<Int>
    val currentCreep: Creep?
    val spawnProgress: Float

    fun queueCreep(creep: Creep): Boolean
    fun processQueue(delta: Double) {}
}