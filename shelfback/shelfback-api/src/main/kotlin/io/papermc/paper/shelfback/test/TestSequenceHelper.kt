package io.papermc.paper.shelfback.test

import io.papermc.paper.shelfback.continuation.TestContinuation

/**
 * The test sequence helper is responsible for helper methods.
 */
interface TestSequenceHelper {

    fun sleep(ticksToSleep: Int): TestContinuation

}