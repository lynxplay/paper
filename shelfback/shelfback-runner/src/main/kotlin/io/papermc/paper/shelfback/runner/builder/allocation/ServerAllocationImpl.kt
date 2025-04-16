package io.papermc.paper.shelfback.runner.builder.allocation

import io.papermc.paper.configuration.GlobalConfiguration
import io.papermc.paper.shelfback.builder.ServerAllocation

class ServerAllocationImpl : ServerAllocation, TestAllocation<ServerAllocation> {
    private var paperGlobalInit: (GlobalConfiguration.() -> Unit)? = null

    override fun paperGlobal(init: GlobalConfiguration.() -> Unit): ServerAllocation {
        this.paperGlobalInit = init
        return this
    }

    override fun canBeBatched(other: TestAllocation<*>): Boolean {
        // We cannot batch a server allocation with anything else.
        // It might change configurations other things rely on.
        return false
    }

    override fun into(): ServerAllocation {
        return ServerAllocationImpl()
    }
}
