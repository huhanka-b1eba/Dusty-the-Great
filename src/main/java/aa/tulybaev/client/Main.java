package aa.tulybaev.client;

import aa.tulybaev.client.core.GameLoop;
import aa.tulybaev.client.core.SnapshotBuffer;
import aa.tulybaev.client.input.InputHandler;
import aa.tulybaev.client.model.world.World;
import aa.tulybaev.client.network.NetworkClient;
import aa.tulybaev.client.ui.GameFrame;
import aa.tulybaev.client.ui.GamePanel;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Модель мира (ТОЛЬКО визуал)
        World world = new World();

        // 2. Буфер снапшотов
        SnapshotBuffer snapshotBuffer = new SnapshotBuffer();

        // 3. Ввод
        InputHandler input = new InputHandler();

        // 4. Сеть
        NetworkClient network = new NetworkClient(snapshotBuffer);

        // 5. UI
        GamePanel panel = new GamePanel(world);
        panel.addKeyListener(input);
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        GameFrame frame = new GameFrame(panel);
        frame.setVisible(true);

        // 6. Игровой цикл
        GameLoop loop = new GameLoop(
                world,
                panel,
                network,
                snapshotBuffer,
                input
        );

        new Thread(loop, "GameLoop").start();
    }
}
