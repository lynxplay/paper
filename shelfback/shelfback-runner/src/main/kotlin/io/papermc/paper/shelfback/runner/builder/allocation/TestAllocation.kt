package io.papermc.paper.shelfback.runner.builder.allocation

/**
 * A common parent type of allocations.
 */
interface TestAllocation<Into> {

    /**
     * Determines if this allocation can be batched with another allocation.
     */
    fun canBeBatched(other: TestAllocation<*>): Boolean {
        return false
    }

    /**
     * An arbitrary weight scaling for the allocation defining how relative
     * processing power.
     */
    fun weight(): Int {
        return 1;
    }

    fun into(): Into
}