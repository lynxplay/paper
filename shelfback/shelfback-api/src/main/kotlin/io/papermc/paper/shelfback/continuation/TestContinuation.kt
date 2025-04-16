package io.papermc.paper.shelfback.continuation

/**
 * A shelfback continuation is a temporary state in which a test can be stalled/awaits continuation.
 */
interface TestContinuation {

    /**
     * Pulls if this continuation is marked as completed.
     */
    fun completed(): Boolean
}
