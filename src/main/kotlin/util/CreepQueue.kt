package util

import entity.Creep
import gui.CREEP_TYPES_COUNT
import java.util.*

private const val MAX_QUEUE_SIZE = 5

class CreepQueue(
	creepTypesCount: Int = CREEP_TYPES_COUNT,
	private val maxQueueSize: Int = MAX_QUEUE_SIZE
) {

	private val _count = mutableListOf<Int>()
	val count: List<Int>
		get() = _count

	private val queue = LinkedList<Creep>()

	init {
		repeat(creepTypesCount) {
			_count.add(0)
		}
	}

	/**
	 * @return true если успешно добавлено в очередь
	 */
	fun push(creep: Creep): Boolean {
		val i = creep.type - 1
		val canPush = count[i] < maxQueueSize
		if (canPush) {
			_count[i] = count[i] + 1
			queue.addLast(creep)
		}

		return canPush
	}

	fun peek(): Creep? = queue.peekFirst()

	fun pop(): Creep? {
		val creep = peek()
		return creep?.also {
			val i = creep.type - 1
			_count[i] = count[i] - 1
			queue.removeFirst()
		}
	}

	fun isEmpty() = queue.isEmpty()
}