package io.papermc.shelfback

import io.papermc.paper.shelfback.builder.shelfback
import io.papermc.paper.shelfback.test.ShelfbackTest
import org.bukkit.block.BlockType
import kotlin.io.path.Path

class BlockPlaceTest {

    @ShelfbackTest
    fun testSetBlock() = shelfback {
        val schematic = schematic(Path("blocks/place/simple.schem"))
        test {

        }
    }

    @ShelfbackTest
    fun testBreakBlock() = shelfback {
        val schematic = schematic(Path("blocks/destroy/simple.schem"))
        test {

        }
    }

    @ShelfbackTest
    fun testTntDupliucationOption() = shelfback {
        schematic(Path("config/global/tntDuplication.schem"))
        server {
            paperGlobal {
                unsupportedSettings.allowPistonDuplication = true
            }
        }
        test {
            yield(helpers().sleep(10))
            yield(assertions().blockData(0, 3, 0, BlockType.TNT.createBlockData()))
        }
    }
}
