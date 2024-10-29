import entity.Creep
import junit.framework.TestCase.*
import org.junit.Test
import util.CreepQueue

class CreepQueueTest {
    @Test
    fun testCount() {
        val creep1_1 = Creep(1, 1)
        val creep1_2 = Creep(1, 2)
        val creep1_3 = Creep(1, 3)

        val queue = CreepQueue()
        assertEquals(listOf(0, 0, 0, 0), queue.count)
        assertTrue(queue.isEmpty())

        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_2)
        queue.push(creep1_3)
        queue.push(creep1_3)

        assertEquals(listOf(3, 1, 2, 0), queue.count)
        assertFalse(queue.isEmpty())
    }

    @Test
    fun testPeek() {
        val creep1_1 = Creep(1, 1)
        val creep1_2 = Creep(1, 2)
        val creep1_3 = Creep(1, 3)

        val queue = CreepQueue()

        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_2)
        queue.push(creep1_3)
        queue.push(creep1_3)

        assertEquals(creep1_1, queue.peek())

        val emptyQueue = CreepQueue()
        assertEquals(null, emptyQueue.peek())
    }

    @Test
    fun testPop() {
        val creep1_1 = Creep(1, 1)
        val creep1_2 = Creep(1, 2)
        val creep1_3 = Creep(1, 3)

        val queue = CreepQueue()

        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_2)
        queue.push(creep1_3)
        queue.push(creep1_3)

        assertEquals(creep1_1, queue.pop())
        assertEquals(creep1_1, queue.pop())
        assertEquals(creep1_1, queue.pop())
        assertEquals(listOf(0, 1, 2, 0), queue.count)

        assertEquals(creep1_2, queue.pop())
        assertEquals(listOf(0, 0, 2, 0), queue.count)

        assertEquals(creep1_3, queue.pop())
        assertEquals(creep1_3, queue.pop())
        assertEquals(listOf(0, 0, 0, 0), queue.count)

        val emptyQueue = CreepQueue()
        assertEquals(null, emptyQueue.pop())
    }

    @Test
    fun testPush() {
        val creep1_1 = Creep(1, 1)
        val creep1_3 = Creep(1, 3)
        val creep2_1 = Creep(2, 1)

        val queue = CreepQueue()

        queue.push(creep1_1)
        queue.push(creep1_1)
        queue.push(creep1_1)

        queue.push(creep1_3)
        queue.push(creep1_3)

        queue.push(creep2_1)
        assertTrue(queue.push(creep2_1))
        assertFalse(queue.push(creep2_1))

        assertEquals(listOf(5, 0, 2, 0), queue.count)
    }
}