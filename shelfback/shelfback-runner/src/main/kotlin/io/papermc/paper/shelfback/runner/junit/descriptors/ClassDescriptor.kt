package io.papermc.paper.shelfback.runner.junit.descriptors

import org.junit.platform.engine.TestDescriptor.Type
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.ClassSource

/**
 * Creates a new class descriptor.
 */
fun classDescriptor(parentId: UniqueId, javaClass: Class<*>): ClassDescriptor {
    return ClassDescriptor(parentId.append("class", javaClass.name), javaClass.name, ClassSource.from(javaClass))
}

class ClassDescriptor(uniqueId: UniqueId?, displayName: String?, source: TestSource?) :
    AbstractTestDescriptor(uniqueId, displayName, source) {

    override fun getType(): Type = Type.CONTAINER
}
