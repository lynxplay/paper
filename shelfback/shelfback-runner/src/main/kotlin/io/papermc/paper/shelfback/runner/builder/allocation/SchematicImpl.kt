package io.papermc.paper.shelfback.runner.builder.allocation

import io.papermc.paper.math.BlockPosition
import io.papermc.paper.shelfback.builder.allocation.Schematic
import org.bukkit.World
import java.nio.file.Path

class SchematicImpl(path: Path) : Schematic {

    override fun corner(): BlockPosition {
        TODO("Not yet implemented")
    }

    override fun world(): World {
        TODO("Not yet implemented")
    }
}