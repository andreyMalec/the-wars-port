package util

import kotlin.random.Random

@JvmInline
value class Chance(private val percent: Double) {
	val proc: Boolean
		get() = Random.nextDouble() < percent
}