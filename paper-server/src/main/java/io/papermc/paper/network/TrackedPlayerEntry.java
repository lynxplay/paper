package io.papermc.paper.network;

import java.util.UUID;

/**
 * A tracked player entry enables {@link net.minecraft.server.level.ServerEntity} and
 * {@link net.minecraft.server.level.ChunkMap.TrackedEntity} instances to carry additional data about "tracked" entities.
 */
public class TrackedPlayerEntry {

    private boolean hidden = false;

    /**
     * Defines if this entry is hidden.
     * A hidden entry is tracked on the server side, the remote client does not receive packets and is not aware
     * of the entity tracking this entry.
     *
     * @return {@code true} if this entry is hidden from the player.
     */
    public boolean hidden() {
        return this.hidden;
    }

    /**
     * Defines if this entry belongs to a hidden from player or not.
     *
     * @param hide if this entry maps a player that is hidden.
     */
    public TrackedPlayerEntry hide(final boolean hide) {
        this.hidden = hide;
        return this;
    }
}
