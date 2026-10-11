package io.papermc.paper.shelfback;

import java.util.ServiceLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ShelfbackHook {

    ShelfbackHook NOOP = new ShelfbackHook() {
    };

    ShelfbackHook INSTANCE = ServiceLoader.load(ShelfbackHook.class).findFirst().orElse(NOOP);

    default void craftbukkitMain() {

    }

    default void preBootstrap() {

    }

    default void postMinecraftServerSpin(final DedicatedServer dedicatedServer) {

    }

    default void tickStart(final MinecraftServer minecraftServer) {

    }

    default void tickEnd(final MinecraftServer minecraftServer) {

    }
}
