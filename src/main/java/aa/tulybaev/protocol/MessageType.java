package aa.tulybaev.protocol;

public enum MessageType {
    JOIN(1),
    INPUT(2),
    SNAPSHOT(3),
    DISCONNECT(4);

    private final byte id;

    MessageType(int id) {
        this.id = (byte) id;
    }

    public byte id() {
        return id;
    }

    public static MessageType from(byte id) {
        for (MessageType t : values()) {
            if (t.id == id) return t;
        }
        throw new IllegalArgumentException("Unknown type: " + id);
    }
}

