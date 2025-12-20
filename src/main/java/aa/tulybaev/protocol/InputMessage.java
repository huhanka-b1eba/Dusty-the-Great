package aa.tulybaev.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class InputMessage implements GameMessage {

    private final int playerId;
    private final float dx;
    private final float dy;
    private final boolean shoot;

    public InputMessage(int playerId, float dx, float dy, boolean shoot) {
        this.playerId = playerId;
        this.dx = dx;
        this.dy = dy;
        this.shoot = shoot;
    }

    @Override
    public MessageType type() {
        return MessageType.INPUT;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(playerId);
        out.writeFloat(dx);
        out.writeFloat(dy);
        out.writeBoolean(shoot);
    }

    public static InputMessage read(DataInputStream in) throws IOException {
        return new InputMessage(
                in.readInt(),
                in.readFloat(),
                in.readFloat(),
                in.readBoolean()
        );
    }

    public int playerId() { return playerId; }
    public float dx() { return dx; }
    public float dy() { return dy; }
    public boolean shoot() { return shoot; }
}