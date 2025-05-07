package io.papermc.paper.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.server.network.ServerPlayerConnection;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The {@link SeenByTracker} is responsible for managing tracked players in the {@link net.minecraft.server.level.ChunkMap.TrackedEntity}
 * and {@link net.minecraft.server.level.ServerEntity} logic.
 * <p>
 * It aims to efficiently implement numerous API reasons entities may technically be tracked by a vanilla entity, however
 * are not visible to the end player.
 * Previous solutions would simply not add players to the tracked lists, resulting in repeated attempted re-tracking
 * of players that were not allowed to track said entity.
 */
@NullMarked
public class SeenByTracker implements Iterable<ServerPlayerConnection> {

    static class IndexReferenced {
        public final TrackedPlayerEntry trackedPlayerEntry;
        public int index = -1;

        IndexReferenced(final TrackedPlayerEntry trackedPlayerEntry) {
            this.trackedPlayerEntry = trackedPlayerEntry;
        }
    }

    private final Object2ObjectOpenHashMap<ServerPlayerConnection, @Nullable IndexReferenced> trackedPlayers = new Object2ObjectOpenHashMap<>();

    /**
     * A cached representation of the {@link #trackedPlayers}'s key set filtered to {@link TrackedPlayerEntry} instances
     * that are not {@link TrackedPlayerEntry#hidden()}.
     * This allows for fast iterations that do not require filtering the entry set of tracked players.
     */
    private final ObjectArrayList<ServerPlayerConnection> nonHiddenTracked = new ObjectArrayList<>();

    /**
     * Default iteration to the non-hidden players, compatibility with mojangs Set, which also only includes
     * entities shown to the player.
     */
    @Override
    public @NotNull Iterator<ServerPlayerConnection> iterator() {
        return nonHiddenTracked.iterator();
    }

    /**
     * Similar to the iterator, we consider the tracker empty if none of the entities are actually shown.
     *
     * @return {@code true} if the tracker has no non-hidden entries.
     */
    public boolean hasNonHiddenEntries() {
        return !this.nonHiddenTracked.isEmpty();
    }

    public boolean hasAnyEntries() {
        return !this.trackedPlayers.isEmpty();
    }

    public int nonHiddenEntriesCount() {
        return this.nonHiddenTracked.size();
    }

    public int entriesCount() {
        return this.trackedPlayers.size();
    }

    /**
     * Similar to the iterator, we consider the tracker stream to be the one of the entities are actually shown.
     *
     * @return a stream of all non-hidden elements in this tracker.
     */
    public Stream<ServerPlayerConnection> streamNonHiddenEntries() {
        return this.nonHiddenTracked.stream();
    }

    /**
     * Yields a collection of *all* server side tracked player connections.
     *
     * @return the collection.
     */
    public Collection<ServerPlayerConnection> allServerSide() {
        return this.trackedPlayers.keySet();
    }

    /**
     * Computes if this tracker is aware of the passed player connection and the player is actively tracking the entity.
     * Hidden players are not considered.
     *
     * @param serverPlayerConnection the connection to query for.
     * @return {@code true} if the connection is known to the tracker and is not hidden.
     */
    public boolean contains(final ServerPlayerConnection serverPlayerConnection) {
        final IndexReferenced indexReferenced = this.trackedPlayers.get(serverPlayerConnection);
        return indexReferenced != null && !indexReferenced.trackedPlayerEntry.hidden();
    }

    /**
     * Computes if this tracker is aware of the passed player connection.
     * This does not imply that the connection is currently non-hidden.
     *
     * @param serverPlayerConnection the connection to query for.
     * @return {@code true} if the connection is known to the tracker on the server side.
     * This includes both hidden and non-hidden entries.
     */
    public boolean containsServerSide(final ServerPlayerConnection serverPlayerConnection) {
        return this.trackedPlayers.containsKey(serverPlayerConnection);
    }

    /**
     * Updates the passed connection's tracked entry if present.
     * If the value is present, a recomputation of the cached iteration array will be performed.
     *
     * @param serverPlayerConnection the server connection.
     * @param updater                the update function for the entry type.
     */
    public void updateIfPresent(
        final ServerPlayerConnection serverPlayerConnection,
        final Consumer<TrackedPlayerEntry> updater
    ) {
        final IndexReferenced indexReferenced = this.trackedPlayers.get(serverPlayerConnection);
        if (indexReferenced == null) return; // Not tracked, nothing to update.

        final TrackedPlayerEntry trackedPlayerEntry = indexReferenced.trackedPlayerEntry;
        final boolean previousHidden = trackedPlayerEntry.hidden();
        updater.accept(trackedPlayerEntry);
        final boolean newHidden = trackedPlayerEntry.hidden();
        if (previousHidden == trackedPlayerEntry.hidden()) return; // Hidden did not change, no need to update.

        if (newHidden && indexReferenced.index != -1) {
            removeFromNonHiddenCache(indexReferenced);
        } else if (indexReferenced.index == -1) {
            indexReferenced.index = nonHiddenTracked.size();
            nonHiddenTracked.add(serverPlayerConnection);
        }
    }

    private void removeFromNonHiddenCache(final IndexReferenced indexReferenced) {
        // We can avoid array copies here as iteration order does not need to be consistent with insertion order
        // Last element, simply clear, we cannot move last element to removed index
        final int size = nonHiddenTracked.size();
        if (size == 1) {
            nonHiddenTracked.clear();
            return;
        }

        final ServerPlayerConnection tail = nonHiddenTracked.get(size - 1);
        this.trackedPlayers.get(tail).index = indexReferenced.index;
        nonHiddenTracked.set(indexReferenced.index, tail);
        indexReferenced.index = -1;

        nonHiddenTracked.size(size - 1);
    }

    /**
     * Adds a new tracked player entry to this tracker for the specific connection.
     *
     * @param serverPlayerConnection the player connection to add.
     * @param configurator     the metadata player entry.
     */
    public boolean addAndNeedsPairing(
        final ServerPlayerConnection serverPlayerConnection,
        final Consumer<TrackedPlayerEntry> configurator
    ) {
        if (this.trackedPlayers.containsKey(serverPlayerConnection)) return false;

        final TrackedPlayerEntry trackedPlayerEntry = new TrackedPlayerEntry();
        configurator.accept(trackedPlayerEntry);

        final IndexReferenced indexReferenced = new IndexReferenced(trackedPlayerEntry);
        this.trackedPlayers.put(serverPlayerConnection, indexReferenced);
        if (trackedPlayerEntry.hidden()) {
            indexReferenced.index = -1;
            return false;
        }

        this.nonHiddenTracked.add(serverPlayerConnection);
        indexReferenced.index = nonHiddenTracked.size() - 1;
        return true;
    }

    public boolean remove(final ServerPlayerConnection serverPlayerConnection) {
        final IndexReferenced remove = this.trackedPlayers.remove(serverPlayerConnection);
        if (remove == null || remove.index == -1) return false;

        removeFromNonHiddenCache(remove);
        return !remove.trackedPlayerEntry.hidden();
    }
}
