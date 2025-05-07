package io.papermc.paper.network;

import com.google.common.collect.Iterables;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NullMarked
@Normal
class SeenByTrackerTest {

    @Test
    public void testUpdatingToHidden() {
        final SeenByTracker seenByTracker = new SeenByTracker();
        seenByTracker.addAndNeedsPairing(new MockConnection("a"), t -> {});
        seenByTracker.addAndNeedsPairing(new MockConnection("b"), t -> {});
        seenByTracker.addAndNeedsPairing(new MockConnection("c"), t -> {});
        seenByTracker.addAndNeedsPairing(new MockConnection("d"), t -> {});

        assertFalse(seenByTracker.hasNonHiddenEntries());
        assertEquals(4, Iterables.size(seenByTracker));
    }

    record MockConnection(
        String name
    ) implements ServerPlayerConnection {

        @Override
        public ServerPlayer getPlayer() {
            throw new UnsupportedOperationException("mock");
        }

        @Override
        public void send(final Packet<?> packet) {
        }
    }

}
