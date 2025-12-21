package aa.tulybaev.client;

import aa.tulybaev.client.core.GameLoop;
import aa.tulybaev.client.core.SnapshotBuffer;
import aa.tulybaev.client.input.InputHandler;
import aa.tulybaev.client.model.world.World;
import aa.tulybaev.client.network.NetworkClient;
import aa.tulybaev.client.ui.GameFrame;
import aa.tulybaev.client.ui.GamePanel;

import javax.swing.*;

public class Main {

    private static GameFrame frame;
    private static GameLoop gameLoop;
    private static Thread gameThread;
    private static NetworkClient network;
    private static GameState gameState;

    public static void main(String[] args) throws Exception {
        gameState = GameStateStorage.load();
        System.out.println("Loaded lastPlayerId: " + gameState.lastPlayerId);

        World world = new World();
        SnapshotBuffer snapshotBuffer = new SnapshotBuffer();
        InputHandler input = new InputHandler();
        GamePanel panel = new GamePanel(world);
        panel.addKeyListener(input);
        panel.setFocusable(true);

        frame = new GameFrame();
        // Передаём ссылки в GameFrame
        frame.startGame = Main::startGameImpl;
        frame.restartGame = Main::restartGameImpl;

        frame.setVisible(true);
        frame.showMenu(); // Начинаем с меню
    }

    // Реализация GameFrame.startGame
    public static void startGameImpl() {
        try {
            World world = new World();
            SnapshotBuffer snapshotBuffer = new SnapshotBuffer();
            InputHandler input = new InputHandler();
            network = new NetworkClient(snapshotBuffer);
            network.setConnectionCallback(id -> world.setLocalPlayerId(id));

            GamePanel panel = new GamePanel(world);
            panel.addKeyListener(input);
            panel.setFocusable(true);
            panel.requestFocusInWindow();

            gameLoop = new GameLoop(world, panel, frame, network, snapshotBuffer, input);
            gameThread = new Thread(gameLoop, "GameLoop");
            gameThread.start();

            frame.setGamePanel(panel);
            frame.showGame();
            SwingUtilities.invokeLater(() -> {
                panel.requestFocusInWindow();
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Реализация GameFrame.restartGame
    public static void restartGameImpl() {
        // Останавливаем текущую игру
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
        if (network != null) {
            network.shutdown();
            network = null;
        }

        // Сохраняем результат
        if (network != null && network.getPlayerId() >= 0) {
            gameState.lastPlayerId = network.getPlayerId();
            GameStateStorage.save(gameState);
        }

        // Запускаем новую игру
        startGameImpl();
    }

    // Метод для GameOver из GameLoop
    public static void triggerGameOver() {
        SwingUtilities.invokeLater(() -> frame.showGameOver());
    }
}