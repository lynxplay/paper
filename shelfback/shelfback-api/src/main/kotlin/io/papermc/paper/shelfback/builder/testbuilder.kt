package io.papermc.paper.shelfback.builder

import io.papermc.paper.configuration.GlobalConfiguration
import io.papermc.paper.shelfback.TestSequenceContext
import io.papermc.paper.shelfback.builder.allocation.Schematic
import io.papermc.paper.shelfback.builder.allocation.SchematicAllocation
import io.papermc.paper.shelfback.continuation.TestContinuation
import io.papermc.paper.shelfback.provider
import java.nio.file.Path
import kotlin.coroutines.RestrictsSuspension

/**
 * Static access function to create test configurations.
 */
fun shelfback(init: ShelfbackTestConfiguration.() -> Unit): ShelfbackTestConfiguration {
    return provider.newTestConfiguration().apply(init)
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
@DslMarker
annotation class ShelfbackDslMarker

/**
 * A scope for constructing a test sequence.
 */
@ShelfbackDslMarker
@RestrictsSuspension
interface TestSequenceScope {

    /**
     * Yields the continuation back up to the test framework
     */
    suspend fun yield(continuation: TestContinuation)
}

/**
 * A type alias for the initializer that creates/inits/configures a test sequence scope.
 */
typealias TestSequenceContextInitializer = suspend TestSequenceContext.() -> Unit

/**
 * The shelfback test configuration type allows tests to configure themselves.
 */
@ShelfbackDslMarker
interface ShelfbackTestConfiguration : Allocations {
    /**
     * Defines the test logic.
     */
    fun test(block: TestSequenceContextInitializer)
}

/**
 * Builder for allocations required by a test.
 */
@ShelfbackDslMarker
interface Allocations {

    /**
     * Allocates a separate server for this test from the default server.
     *
     * Mostly used when testing configuration values.
     */
    fun server(init: ServerAllocation.() -> Unit = {})

    /**
     * Allocates a schematic at the given path.
     */
    fun schematic(path: Path, init: SchematicAllocation.() -> Unit = {}): Schematic
}

/**
 * Builder for a separate server allocation.
 */
@ShelfbackDslMarker
interface ServerAllocation {
    /**
     * Configures the paper-global.yml with the specific configuration overrides.
     */
    fun paperGlobal(init: GlobalConfiguration.() -> Unit): ServerAllocation
}
