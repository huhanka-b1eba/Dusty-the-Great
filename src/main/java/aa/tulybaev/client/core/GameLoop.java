package aa.tulybaev.client.core;

import aa.tulybaev.client.input.InputHandler;
import aa.tulybaev.client.model.entity.Player;
import aa.tulybaev.client.model.world.World;
import aa.tulybaev.client.network.NetworkClient;
import aa.tulybaev.client.ui.GamePanel;

import java.util.List;

public final class GameLoop implements Runnable {

    private static final int FPS = 60;

    private final World world;
    private final GamePanel panel;
    private final NetworkClient network;
    private final SnapshotBuffer snapshotBuffer;
    private final InputHandler input;
    private final Player localPlayer;

    private int renderTick = 0;

    public GameLoop(
            World world,
            GamePanel panel,
            NetworkClient network,
            SnapshotBuffer snapshotBuffer,
            InputHandler input
    ) {
        this.world = world;
        this.panel = panel;
        this.network = network;
        this.snapshotBuffer = snapshotBuffer;
        this.input = input;

        // Создаём локального игрока (временно, позже позиция будет из снапшота)
        this.localPlayer = new Player(100, 300); // начальная позиция
    }

    @Override
    public void run() {
        long nsPerFrame = 1_000_000_000L / FPS;
        long last = System.nanoTime();

        while (true) {
            long now = System.nanoTime();

            if (now - last >= nsPerFrame) {
                renderTick++;

                // 1. Обновляем логику локального игрока
                localPlayer.update(
                        input,
                        List.of(),
                        world.getGroundY(),
                        world.getPlatforms()  // ← теперь возвращает List<Platform>
                );

                // 2. Отправляем ввод (или позицию) на сервер
                network.sendInput(
                        localPlayer.getX(),
                        localPlayer.getY(),
                        localPlayer.isFacingRight() ? 1 : 0  // ← facing как 1/0
                );

                // 3. Получаем снапшоты и обновляем визуальное состояние мира
                InterpolatedSnapshot snap = snapshotBuffer.getInterpolated(renderTick);
                if (snap != null) {
                    world.applyInterpolated(snap);
                }

                // 4. Передаём данные HUD в панель (или напрямую в Renderer)
                panel.setHudData(
                        localPlayer.getHp(),
                        localPlayer.getMaxHp(),
                        localPlayer.getAmmo(),
                        localPlayer.isShooting()
                );

                // 5. Рендер
                panel.repaint();

                last = now;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
