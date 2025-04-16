package io.papermc.paper.shelfback.runner.junit.discovery

import io.papermc.paper.shelfback.runner.junit.descriptors.methodDescriptor
import org.junit.platform.commons.annotation.Testable
import org.junit.platform.commons.util.AnnotationUtils
import org.junit.platform.engine.discovery.MethodSelector
import org.junit.platform.engine.support.discovery.SelectorResolver
import org.junit.platform.engine.support.discovery.SelectorResolver.Context
import org.junit.platform.engine.support.discovery.SelectorResolver.Match.exact
import org.junit.platform.engine.support.discovery.SelectorResolver.Resolution
import java.util.Optional.of

class MethodSelectorResolver : SelectorResolver {

    /**
     * Resolves a method to a resolution.
     */
    override fun resolve(selector: MethodSelector, context: Context): Resolution {
        val javaMethod = selector.javaMethod

        if (!AnnotationUtils.isAnnotated(javaMethod, Testable::class.java)) return Resolution.unresolved()
        return Resolution.match(exact(context.addToParent { of(methodDescriptor(it.uniqueId, javaMethod)) }.get()))
    }
}
