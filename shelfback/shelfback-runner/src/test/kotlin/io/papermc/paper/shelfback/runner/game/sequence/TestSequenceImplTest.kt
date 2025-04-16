package io.papermc.paper.shelfback.runner.game.sequence

import io.papermc.paper.shelfback.continuation.TestContinuation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestSequenceImplTest {

    internal data class TestTestContinuation(val data: Int) : TestContinuation {
        override fun completed(): Boolean {
            return true
        }
    }

    @Test
    fun `test iterator correct behaviour`() {
        val impl = testSequenceImplFrom {
            for (i in 1..10) {
                yield(TestTestContinuation(i))
            }
        }

        var last = 0
        while (impl.hasNext()) {
            assertEquals(++last, (impl.next() as TestTestContinuation).data)
        }

        assertFalse(impl.hasNext()) // Test if I can call hasNext again
    }

    @Test
    fun `test lazy evaluation`() {
        var valueToBeMutated = true

        val impl = testSequenceImplFrom {
            yield(TestTestContinuation(3))
            valueToBeMutated = false
            yield(TestTestContinuation(6))
            valueToBeMutated = true
        }

        assertTrue(impl.hasNext())
        assertEquals(3, (impl.next() as TestTestContinuation).data)
        assertTrue(valueToBeMutated)

        assertTrue(impl.hasNext())
        assertEquals(6, (impl.next() as TestTestContinuation).data)
        assertFalse(valueToBeMutated)

        assertFalse(impl.hasNext())
        assertTrue(valueToBeMutated)
    }
}
