package io.papermc.paper.shelfback.runner.junit

import io.papermc.paper.shelfback.runner.junit.batch.batchTests
import io.papermc.paper.shelfback.runner.junit.discovery.ClassSelectorResolver
import io.papermc.paper.shelfback.runner.junit.discovery.MethodSelectorResolver
import org.junit.platform.engine.*
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver

class ShelfbackJunitEngine : TestEngine {
    override fun getId(): String = "papermc:shelfback"

    override fun discover(discoveryRequest: EngineDiscoveryRequest?, uniqueId: UniqueId?): TestDescriptor {
        val root = EngineDescriptor(uniqueId, "shelfback")

        val resolver = EngineDiscoveryRequestResolver.builder<TestDescriptor>()
            .addSelectorResolver(ClassSelectorResolver())
            .addSelectorResolver(MethodSelectorResolver())
            .build()

        resolver.resolve(discoveryRequest, root)
        return root
    }

    override fun execute(request: ExecutionRequest) {
        val rootTestDescriptor = request.rootTestDescriptor

        val batchTests = batchTests(rootTestDescriptor)
        batchTests.size
    }
}
