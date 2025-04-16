package io.papermc.paper.shelfback.runner.junit.descriptors

import org.junit.platform.engine.TestDescriptor.Type
import org.junit.platform.engine.TestSource
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.MethodSource
import java.lang.reflect.Method

/**
 * Creates a new method descriptor.
 */
fun methodDescriptor(parentId: UniqueId, javaRef: Method): MethodDescriptor {
    return MethodDescriptor(
        parentId.append("method", "${javaRef.name}(${javaRef.parameterTypes.map { it.name }})"),
        javaRef.name,
        MethodSource.from(
            javaRef.declaringClass.name,
            javaRef.name,
            *javaRef.parameterTypes
        ),
        javaRef
    )
}

class MethodDescriptor(uniqueId: UniqueId?, displayName: String?, source: TestSource?, val javaRef: Method) :
    AbstractTestDescriptor(uniqueId, displayName, source) {

    override fun getType(): Type = Type.TEST
}
