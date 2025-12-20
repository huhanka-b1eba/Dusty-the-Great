package aa.tulybaev.protocol;

import java.io.*;
import java.util.*;

public final class WorldSnapshotMessage implements GameMessage {

    private final int tick;
    private final List<PlayerSnapshot> players;

    public WorldSnapshotMessage(int tick, List<PlayerSnapshot> players) {
        this.tick = tick;
        this.players = List.copyOf(players);
    }

    @Override
    public MessageType type() {
        return MessageType.SNAPSHOT;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(tick);
        out.writeInt(players.size());
        for (PlayerSnapshot p : players) {
            p.write(out);
        }
    }

    public static WorldSnapshotMessage read(DataInputStream in) throws IOException {
        int tick = in.readInt();
        int count = in.readInt();

        List<PlayerSnapshot> players = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            players.add(PlayerSnapshot.read(in));
        }

        return new WorldSnapshotMessage(tick, players);
    }

    public int tick() { return tick; }
    public List<PlayerSnapshot> players() { return players; }
}
