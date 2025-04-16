package io.papermc.paper.shelfback.runner.test

import io.papermc.paper.shelfback.TestSequenceContext
import io.papermc.paper.shelfback.builder.TestSequenceScope
import io.papermc.paper.shelfback.test.TestSequenceAssertions
import io.papermc.paper.shelfback.test.TestSequenceHelper

class TestSequenceContextImpl(
    testSequenceScope: TestSequenceScope,
    private val assertions: TestSequenceAssertions,
    private val helpers: TestSequenceHelper
) : TestSequenceContext, TestSequenceScope by testSequenceScope {
    /**
     * Yields an assertion helper used to assert game state.
     */
    override fun assertions(): TestSequenceAssertions {
        return assertions
    }

    /**
     * Yields a general game helper used for general utility purposes.
     */
    override fun helpers(): TestSequenceHelper {
        return helpers
    }
}