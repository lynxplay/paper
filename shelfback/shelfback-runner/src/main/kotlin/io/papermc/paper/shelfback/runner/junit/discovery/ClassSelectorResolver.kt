package io.papermc.paper.shelfback.runner.junit.discovery

import io.papermc.paper.shelfback.runner.junit.descriptors.classDescriptor
import org.junit.platform.commons.util.ReflectionUtils
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod
import org.junit.platform.engine.support.discovery.SelectorResolver
import org.junit.platform.engine.support.discovery.SelectorResolver.Context
import org.junit.platform.engine.support.discovery.SelectorResolver.Resolution
import org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.match
import org.junit.platform.engine.support.discovery.SelectorResolver.Resolution.unresolved
import java.util.Optional.of

class ClassSelectorResolver : SelectorResolver {

    /**
     * Resolves a class selector to its resolution.
     */
    override fun resolve(selector: ClassSelector, context: Context): Resolution {
        val clazz = selector.javaClass
        if (!clazz.name.endsWith("Test")) return unresolved()

        return match(
            SelectorResolver.Match.exact(
                context.addToParent { of(classDescriptor(it.uniqueId, clazz)) }.get()
            ) {
                ReflectionUtils.findMethods(clazz) { true }
                    .map { selectMethod(clazz, it) }
                    .toSet()
            }
        )
    }
}
