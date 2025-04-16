@file:Suppress("UnstableApiUsage")

package io.papermc.paper.shelfback.runner.junit.batch

import com.google.common.graph.GraphBuilder
import io.papermc.paper.shelfback.runner.builder.ShelfbackTestConfigurationImpl
import io.papermc.paper.shelfback.runner.builder.allocation.TestAllocation
import io.papermc.paper.shelfback.runner.junit.descriptors.MethodDescriptor
import io.papermc.paper.shelfback.runner.junit.test.AllocatedTest
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.junit.platform.engine.TestDescriptor

/**
 * Constructs the collection of test batches based on the root test descriptor.
 */
fun batchTests(root: TestDescriptor, maximumBatchWeight: Int = 10): Collection<TestBatch> {
    // Construct a graph of batch-able tests from the root descriptor
    val graph = GraphBuilder.undirected().allowsSelfLoops(false).build<AllocatedTest>()
    val unvisitedNodes = ObjectArrayList(listOf(root))
    while (unvisitedNodes.isNotEmpty()) {
        val descriptor = unvisitedNodes.pop()
        unvisitedNodes.addAll(descriptor.children)
        if (!descriptor.isTest) continue // Do not process non tests

        val allocatedTest = AllocatedTest(descriptor, collectAllocations(descriptor))
        graph.addNode(allocatedTest)
        graph.nodes()
            .filter { it != allocatedTest }
            .filter { it.allocations.all { a -> allocatedTest.allocations.all { b -> a.canBeBatched(b) } } }
            .forEach { graph.putEdge(allocatedTest, it) }
    }

    // Extract batches from said graph, starting with the nodes that can be batched the most (have the most out degree)
    val sortedBatchingNodes = graph.nodes().sortedByDescending { graph.outDegree(it) }.toMutableList()
    val result = ObjectArrayList<TestBatch>()
    while (sortedBatchingNodes.isNotEmpty()) {
        val first = sortedBatchingNodes.first()
        var weight = first.allocationWeight();
        val others = graph.successors(first).takeWhile {
            weight += it.allocationWeight()
            weight < maximumBatchWeight
        }.toMutableList()

        others.add(0, first)
        result.add(TestBatch(others))
        sortedBatchingNodes.removeAll(others)
    }
    return result
}

private fun collectAllocations(test: TestDescriptor): Collection<TestAllocation<*>> {
    if (test !is MethodDescriptor) {
        throw IllegalArgumentException("Not supporting " + test.javaClass.name + " as test descriptors yet!")
    }

    val instance = test.javaRef.declaringClass.getDeclaredConstructor().newInstance()
    val sequence = test.javaRef.invoke(instance) as ShelfbackTestConfigurationImpl

    return sequence.allocations()
}