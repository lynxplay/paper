package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("25w16a")
public class CraftStem extends CraftBlockData implements Ageable {
    private static final IntegerProperty AGE = StemBlock.AGE;

    public CraftStem(BlockState state) {
        super(state);
    }

    @Override
    public int getAge() {
        return this.get(AGE);
    }

    @Override
    public void setAge(final int age) {
        this.set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return AGE.max;
    }
}
