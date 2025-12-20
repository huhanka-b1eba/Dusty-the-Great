package aa.tulybaev.client.model.entity;

import aa.tulybaev.client.render.components.Animation;
import aa.tulybaev.client.render.components.SpriteLoader;

import java.awt.image.BufferedImage;

public final class RemotePlayer implements RenderablePlayer {

    private int x, y;
    private boolean facingRight;
    private int hp;
    private int hitFlashTimer = 0;
    private static final double SCALE = 0.25;

    // ===== ANIMATIONS =====
    private final Animation idle;
    private final Animation walk;
    private Animation current;

    @Override
    public boolean isHit() {
        return hitFlashTimer > 0;
    }

    @Override
    public void advanceAnimation() {
        current.update();
        if (hitFlashTimer > 0) hitFlashTimer--;
    }

    public RemotePlayer(int id) {

        BufferedImage idleImg =
                SpriteLoader.load("/sprites/Player2/Player-2.png");

        BufferedImage walk1 =
                SpriteLoader.load("/sprites/Player2/Player-2-walk-1.png");
        BufferedImage walk2 =
                SpriteLoader.load("/sprites/Player2/Player-2-walk-2.png");

        idle = new Animation(new BufferedImage[]{idleImg}, 30);
        walk = new Animation(new BufferedImage[]{walk1, walk2}, 10);

        current = idle;
    }

    // ================= APPLY SNAPSHOT =================

    public void setState(
            int x,
            int y,
            boolean facingRight,
            int hp
    ) {
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        this.hp = hp;

        current = idle; // default
    }

    // ================= RENDER HELPERS =================

    public void updateAnimation(boolean isMoving) {
        current = isMoving ? walk : idle;
        current.update();
    }

    // ================= GETTERS =================

    public int getDrawX() { return x; }
    public int getDrawY() { return y; }

    public int getWidth() {
        return (int) (current.getFrame().getWidth() * SCALE);
    }

    public int getHeight() {
        return (int) (current.getFrame().getHeight() * SCALE);
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public BufferedImage getFrame() {
        return current.getFrame();
    }

    public int getHp() {
        return hp;
    }

    public void setState(
            float x,
            float y,
            boolean facingRight,
            int hp
    ) {
        this.x = (int) x;
        this.y = (int) y;
        this.facingRight = facingRight;
        this.hp = hp;
    }


}
