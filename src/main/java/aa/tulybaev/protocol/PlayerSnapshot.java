package aa.tulybaev.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PlayerSnapshot {

    private final int id;
    private final float x;
    private final float y;
    private final boolean facingRight;
    private final int hp;

    public PlayerSnapshot(
            int id,
            float x,
            float y,
            boolean facingRight,
            int hp
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        this.hp = hp;
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeBoolean(facingRight);
        out.writeInt(hp);
    }

    public static PlayerSnapshot read(DataInputStream in) throws IOException {
        return new PlayerSnapshot(
                in.readInt(),
                in.readFloat(),
                in.readFloat(),
                in.readBoolean(),
                in.readInt()
        );
    }

    public int id() { return id; }
    public float x() { return x; }
    public float y() { return y; }
    public boolean facingRight() { return facingRight; }
    public int hp() { return hp; }
}
