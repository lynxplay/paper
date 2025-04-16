package io.papermc.paper.shelfback.runner.test

import io.papermc.paper.shelfback.builder.allocation.Schematic
import io.papermc.paper.shelfback.continuation.TestContinuation
import io.papermc.paper.shelfback.test.TestSequenceAssertions
import org.bukkit.block.data.BlockData

class TestSequenceAssertionsImpl : TestSequenceAssertions {
    override fun blockData(
        x: Int,
        y: Int,
        z: Int,
        data: BlockData,
        timeoutTicks: Int,
        schematic: Schematic?
    ): TestContinuation {
        TODO("Not yet implemented")
    }
}