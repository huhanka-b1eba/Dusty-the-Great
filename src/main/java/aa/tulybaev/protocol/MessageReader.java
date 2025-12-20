package aa.tulybaev.protocol;

import java.io.*;

public final class MessageReader {

    private MessageReader() {}

    public static GameMessage read(DataInputStream in) throws IOException {
        MessageType type = MessageType.from(in.readByte());

        return switch (type) {
            case JOIN -> JoinAccept.read(in);
            case INPUT -> InputMessage.read(in);
            case SNAPSHOT -> WorldSnapshotMessage.read(in);
            case DISCONNECT -> throw new IOException("Not implemented");
        };
    }
}
