package io.papermc.paper.shelfback;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import java.util.ServiceLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ShelfbackHook {

    ShelfbackHook NOOP = () -> "no-op";

    ShelfbackHook INSTANCE = Suppliers.memoize(() -> {
        final ShelfbackHook shelfbackHook = ServiceLoader.load(ShelfbackHook.class).findFirst().orElse(NOOP);
        LogUtils.getLogger().info("Using shelfback hook {}", shelfbackHook.identifier()); //todo - move this down to a debug
        return shelfbackHook;
    }).get();

    String identifier();

    default void craftbukkitMain() {

    }

    default void pluginRegistrationHook() {

    }

    default void postMinecraftServerSpin(final DedicatedServer dedicatedServer) {

    }

    default void tickStart(final MinecraftServer minecraftServer) {

    }

    default void tickEnd(final MinecraftServer minecraftServer) {

    }
}
