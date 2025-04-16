package io.papermc.paper.shelfback

import io.papermc.paper.shelfback.builder.TestSequenceScope
import io.papermc.paper.shelfback.test.TestSequenceAssertions
import io.papermc.paper.shelfback.test.TestSequenceHelper

/**
 * The main interface for tests to develop against.
 */
interface TestSequenceContext : TestSequenceScope {

    /**
     * Yields an assertion helper used to assert game state.
     */
    fun assertions(): TestSequenceAssertions

    /**
     * Yields a general game helper used for general utility purposes.
     */
    fun helpers(): TestSequenceHelper
}
