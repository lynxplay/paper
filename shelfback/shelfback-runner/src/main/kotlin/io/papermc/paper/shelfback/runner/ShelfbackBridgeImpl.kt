package io.papermc.paper.shelfback.runner

import io.papermc.paper.shelfback.ShelfbackAPIBridge
import io.papermc.paper.shelfback.builder.ShelfbackTestConfiguration
import io.papermc.paper.shelfback.runner.builder.ShelfbackTestConfigurationImpl

class ShelfbackBridgeImpl : ShelfbackAPIBridge {
    override fun newTestConfiguration(): ShelfbackTestConfiguration {
        return ShelfbackTestConfigurationImpl()
    }
}
