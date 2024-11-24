import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import util.Chance

class ChanceTest {
	@Test
	fun test() {
		val chance = Chance(1.0)
		var r = 0
		repeat(1000) {
			if (chance.proc)
				r += 1
		}
		assertEquals(1000, r)
	}

	@Test
	fun test2() {
		val chance = Chance(0.5)
		val r = repeat1000(chance)
		println("r=$r")
		assertTrue(r in 450..550)
	}

	private fun repeat1000(chance: Chance): Int {
		var r = 0
		repeat(1000) {
			if (chance.proc)
				r += 1
		}
		return r
	}

	@Test
	fun test3() {
		val chance = Chance(0.5)
		var r = 0
		repeat(1000) {
			r += repeat1000(chance)
		}
		val c = r.toDouble() / 1000000
		println("c=$c")
		assertTrue(c in 0.49..0.51)
	}
}