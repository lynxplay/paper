package io.papermc.paper.shelfback.runner.game.sequence

import io.papermc.paper.shelfback.test.TestSequenceAssertions
import io.papermc.paper.shelfback.test.TestSequenceHelper
import io.papermc.paper.shelfback.continuation.TestContinuation
import io.papermc.paper.shelfback.builder.TestSequenceScope
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume

/**
 * Creates a new  test sequence impl from the passed sequence scope configuration.
 */
fun testSequenceImplFrom(block: suspend TestSequenceScope.() -> Unit): TestSequenceImpl {
    val sequenceImpl = TestSequenceImpl()
    sequenceImpl.nextContinuation = block.createCoroutineUnintercepted(sequenceImpl, sequenceImpl)
    return sequenceImpl
}

/**
 * An implementation of the test sequence scope.
 * Heavily inspired by [SequenceBuilderIterator], however without support for iterator yielding as tests generally
 * do not yield *iterators* of continuations.
 */
class TestSequenceImpl : TestSequenceScope, Iterator<TestContinuation>, Continuation<Unit> {

    private var nextValue: TestContinuation? = null
    internal var nextContinuation: Continuation<Unit>? = null
    private var done: Boolean = false


    override fun hasNext(): Boolean {
        awaitNextOrNoneIfDone()
        if (done) return false

        return nextValue != null
    }

    override fun next(): TestContinuation {
        val nextValueToBeReturned = this.nextValue
        this.nextValue = null // Reset it to be populated later
        return nextValueToBeReturned ?: throw NoSuchElementException()
    }

    private fun awaitNextOrNoneIfDone() {
        while (true) {
            if (done || nextValue != null) return

            val stepToBeExecuted = this.nextContinuation!!
            nextContinuation = null
            stepToBeExecuted.resume(Unit)
        }
    }

    override suspend fun yield(continuation: TestContinuation) {
        this.nextValue = continuation
        return suspendCoroutineUninterceptedOrReturn { c ->
            this.nextContinuation = c
            COROUTINE_SUSPENDED
        }
    }

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
        done = true
    }

    // Empty context for the coroutine context
    override val context: CoroutineContext get() = EmptyCoroutineContext
}
