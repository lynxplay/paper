package io.papermc.paper.shelfback.runner.builder.allocation

import io.papermc.paper.shelfback.builder.allocation.Schematic
import io.papermc.paper.shelfback.builder.allocation.SchematicAllocation
import java.nio.file.Path

class SchematicAllocationImpl(
    private val path: Path
) : SchematicAllocation, TestAllocation<Schematic> {
    override fun into(): Schematic {
        return SchematicImpl(path)
    }

    override fun canBeBatched(other: TestAllocation<*>): Boolean {
        return other is SchematicAllocationImpl // We can batch ourselves with other schematic allocations
    }
}