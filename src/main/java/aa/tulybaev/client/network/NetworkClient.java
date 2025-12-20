package aa.tulybaev.client.network;

import aa.tulybaev.client.core.SnapshotBuffer;
import aa.tulybaev.protocol.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Клиентский сетевой слой.
 * Отвечает ТОЛЬКО за отправку/приём сообщений.
 */
public final class NetworkClient {

    private static final int SERVER_PORT = 50000;
    private static final int BUFFER_SIZE = 8192;

    private final DatagramSocket socket;
    private final InetAddress serverAddr;
    private final SnapshotBuffer snapshotBuffer;

    private volatile int playerId = -1;

    public NetworkClient(SnapshotBuffer snapshotBuffer) throws Exception {
        this.snapshotBuffer = snapshotBuffer;
        this.socket = new DatagramSocket();
        this.serverAddr = InetAddress.getByName("localhost");

        sendJoin();
        startListener();
    }

    // ================= JOIN =================

    private void sendJoin() {
        send(new JoinRequest("player"));
    }

    // ================= INPUT =================

    /**
     * Отправка пользовательского ввода.
     * Вызывается из GameLoop.
     */
    // В NetworkClient.java

    public void sendInput(
            float x,
            float y,
            boolean facingRight,
            boolean shoot
    ) {
        if (playerId < 0) return;

        send(new InputMessage(
                playerId,
                x,
                y,
                facingRight,
                shoot
        ));
    }

    // ================= LISTENER =================

    private void startListener() {
        Thread t = new Thread(() -> {
            byte[] buf = new byte[BUFFER_SIZE];

            while (!socket.isClosed()) {
                try {
                    DatagramPacket packet =
                            new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    DataInputStream in =
                            new DataInputStream(
                                    new ByteArrayInputStream(
                                            packet.getData(),
                                            0,
                                            packet.getLength()
                                    )
                            );

                    GameMessage msg = BinaryProtocol.receive(in);
                    handle(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "NetworkListener");

        t.setDaemon(true);
        t.start();
    }

    // ================= HANDLER =================

    private void handle(GameMessage msg) {

        switch (msg.type()) {

            case JOIN -> {
                JoinAccept join = (JoinAccept) msg;
                this.playerId = join.playerId();
                System.out.println("Connected as player " + playerId);
            }


            case SNAPSHOT -> {
                WorldSnapshotMessage snap =
                        (WorldSnapshotMessage) msg;
                snapshotBuffer.push(snap);
            }

            default -> {
                // INPUT и DISCONNECT клиент НЕ принимает
            }
        }
    }

    // ================= SEND =================

    private void send(GameMessage msg) {
        try {
            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();
            DataOutputStream out =
                    new DataOutputStream(baos);

            BinaryProtocol.send(out, msg);

            byte[] data = baos.toByteArray();
            socket.send(
                    new DatagramPacket(
                            data,
                            data.length,
                            serverAddr,
                            SERVER_PORT
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GETTERS =================

    public int getPlayerId() {
        return playerId;
    }
}
