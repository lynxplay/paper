package io.papermc.paper.shelfback.runner.junit.batch

import io.papermc.paper.shelfback.runner.junit.test.AllocatedTest
import org.junit.platform.engine.TestDescriptor

/**
 * A test batch is computed from a collection of [TestDescriptor]
 */
data class TestBatch(val tests: Collection<AllocatedTest>) {

}