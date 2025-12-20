package aa.tulybaev.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

public interface GameMessage {
    MessageType type();
    void write(DataOutputStream out) throws IOException;
}

