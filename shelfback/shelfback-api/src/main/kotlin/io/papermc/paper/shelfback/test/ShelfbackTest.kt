package io.papermc.paper.shelfback.test

import org.junit.platform.commons.annotation.Testable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Testable
annotation class ShelfbackTest()
