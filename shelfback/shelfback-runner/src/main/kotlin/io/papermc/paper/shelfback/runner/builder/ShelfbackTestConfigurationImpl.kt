package io.papermc.paper.shelfback.runner.builder

import io.papermc.paper.shelfback.builder.Allocations
import io.papermc.paper.shelfback.builder.ServerAllocation
import io.papermc.paper.shelfback.builder.ShelfbackTestConfiguration
import io.papermc.paper.shelfback.builder.TestSequenceContextInitializer
import io.papermc.paper.shelfback.builder.allocation.Schematic
import io.papermc.paper.shelfback.builder.allocation.SchematicAllocation
import io.papermc.paper.shelfback.runner.builder.allocation.SchematicAllocationImpl
import io.papermc.paper.shelfback.runner.builder.allocation.ServerAllocationImpl
import io.papermc.paper.shelfback.runner.builder.allocation.TestAllocation
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.nio.file.Path

class ShelfbackTestConfigurationImpl : ShelfbackTestConfiguration, Allocations {

    private val allocations: MutableList<TestAllocation<*>> = mutableListOf()
    private var testSequenceInitializer: TestSequenceContextInitializer? = null

    override fun test(block: TestSequenceContextInitializer) {
        if (testSequenceInitializer != null) throw IllegalStateException("Cannot define a test sequence twice")
        testSequenceInitializer = block
    }

    fun allocations(): List<TestAllocation<*>> {
        return ObjectArrayList(allocations)
    }

    private fun <A, T : TestAllocation<A>> allocation(allocation: T): T {
        allocations.add(allocation)
        return allocation
    }

    override fun server(init: ServerAllocation.() -> Unit) {
        allocation(ServerAllocationImpl()).apply(init)
    }

    override fun schematic(path: Path, init: SchematicAllocation.() -> Unit): Schematic {
        return allocation(SchematicAllocationImpl(path)).apply(init).into()
    }

}
