package io.papermc.paper.shelfback.test

import io.papermc.paper.shelfback.builder.allocation.Schematic
import io.papermc.paper.shelfback.continuation.TestContinuation
import org.bukkit.block.data.BlockData

/**
 * The test sequence helper is responsible for helper methods.
 */
interface TestSequenceAssertions {

    fun blockData(
        x: Int,
        y: Int,
        z: Int,
        data: BlockData,
        timeoutTicks: Int = -1,
        schematic: Schematic? = null
    ): TestContinuation

}