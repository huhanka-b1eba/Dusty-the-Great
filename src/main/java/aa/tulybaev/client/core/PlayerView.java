package aa.tulybaev.client.core;

/**
 * Полный визуальный снимок игрока
 * для интерполяции и рендера.
 */
public record PlayerView(
        int id,
        float x,
        float y,
        boolean facingRight,
        int hp,
        boolean isMoving,      // ← новое
        boolean isOnGround     // ← новое
) {}
