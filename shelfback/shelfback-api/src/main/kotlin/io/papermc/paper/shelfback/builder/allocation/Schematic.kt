package io.papermc.paper.shelfback.builder.allocation

import io.papermc.paper.math.BlockPosition
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

interface Schematic {

    /**
     * Yields the base corner of the schematic in the world as a block position.
     */
    fun corner(): BlockPosition

    /**
     * The world of the schematic
     */
    fun world(): World

    /**
     * Yields the block in the schematic at the relative position passed in.
     */
    fun blockAt(x: Int, y: Int, z: Int): Block {
        return world().getBlockAt(corner().blockX() + x, corner().blockY() + y, corner().blockZ() + z)
    }

    /**
     * Relativises the passed location to 0,0,0 with the corner of this schematic.
     */
    fun relativise(location: Location): Location {
        return location.clone().subtract(corner().x(), corner().y(), corner().z())
    }
}