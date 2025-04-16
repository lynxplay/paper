package io.papermc.paper.shelfback.runner.junit.test

import io.papermc.paper.shelfback.runner.builder.allocation.TestAllocation
import org.junit.platform.engine.TestDescriptor

data class AllocatedTest(
    val descriptor: TestDescriptor,
    val allocations: Collection<TestAllocation<*>>
) {
    fun allocationWeight(): Int {
        return allocations.map { it.weight() }.reduce { a, b -> a + b }
    }
}