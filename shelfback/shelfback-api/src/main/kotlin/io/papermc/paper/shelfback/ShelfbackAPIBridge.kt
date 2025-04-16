package io.papermc.paper.shelfback

import io.papermc.paper.shelfback.builder.ShelfbackTestConfiguration
import java.util.*

internal val provider = ServiceLoader.load(ShelfbackAPIBridge::class.java).single()

/**
 * The API bridge from shelfbacks API to its runner implementation.
 */
interface ShelfbackAPIBridge {

    /**
     * Creates a new empty test configuration.
     */
    fun newTestConfiguration(): ShelfbackTestConfiguration

}
