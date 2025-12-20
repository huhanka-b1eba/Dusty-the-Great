package aa.tulybaev.client.model.entity;

import aa.tulybaev.client.input.InputHandler;
import aa.tulybaev.client.model.world.objects.Platform;
import aa.tulybaev.client.render.components.Animation;
import aa.tulybaev.client.render.components.SoundManager;
import aa.tulybaev.client.render.components.SpriteLoader;

import java.awt.image.BufferedImage;
import java.util.List;

public final class Player implements RenderablePlayer {

    // ================= CONSTANTS =================

    private static final double SCALE = 0.25;

    private static final int MAX_HP = 100;
    private static final int MAX_AMMO = 1000;

    private static final int FIRE_COOLDOWN = 10;
    private static final double BULLET_SPEED = 35;

    private static final double GRAVITY = 0.6;
    private static final double JUMP_VELOCITY = -12;
    private static final double MOVE_SPEED = 4;

    private static final double MUZZLE_X = 0.85;
    private static final double MUZZLE_Y = 0.6;

    // ================= STATE =================

    private double x, y;
    private double vx, vy;

    private boolean onGround;
    private boolean facingRight = true;

    private int hp = MAX_HP;
    private int ammo = MAX_AMMO;

    private int fireCooldown = 0;
    private int hitFlashTimer = 0;
    private int muzzleFlashTimer = 0;

    // ================= ANIMATION =================

    private final Animation idle;
    private final Animation walk;
    private final Animation jump;
    private Animation current;

    // ================= INIT =================

    public Player(double x, double y) {
        this.x = x;
        this.y = y;

        BufferedImage idleImg =
                SpriteLoader.load("/sprites/Player1/Player-1.png");

        BufferedImage walk1 =
                SpriteLoader.load("/sprites/Player1/Player-1-walk-1.png");
        BufferedImage walk2 =
                SpriteLoader.load("/sprites/Player1/Player-1-walk-2.png");

        BufferedImage jumpImg =
                SpriteLoader.load("/sprites/Player1/Player-1-jump.png");

        idle = new Animation(new BufferedImage[]{idleImg}, 30);
        walk = new Animation(new BufferedImage[]{walk1, walk2}, 10);
        jump = new Animation(new BufferedImage[]{jumpImg}, 30);

        current = idle;
    }

    // ================= GAME LOGIC =================

    public void update(
            InputHandler input,
//            List<Bullet> bullets,
            int groundY,
            List<Platform> platforms
    ) {
        // --- movement input ---
        vx = 0;
        if (input.isLeft())  vx = -MOVE_SPEED;
        if (input.isRight()) vx = MOVE_SPEED;

        // --- jump ---
        if (input.isJumpPressed() && onGround) {
            vy = JUMP_VELOCITY;
            input.setJumpPressed(false);
            onGround = false;
        }

        // --- physics ---
        vy += GRAVITY;
        x += vx;
        y += vy;

        onGround = false;

        // --- ground collision ---
        if (y + getHeight() >= groundY) {
            y = groundY - getHeight();
            vy = 0;
            onGround = true;
        }

        // --- platform collision ---
        for (Platform p : platforms) {
            if (vy >= 0 &&
                    x + getWidth() > p.getX() &&
                    x < p.getX() + p.getW() &&
                    y + getHeight() >= p.getY() &&
                    y + getHeight() <= p.getY() + p.getH()
            ) {
                y = p.getY() - getHeight();
                vy = 0;
                onGround = true;
            }
        }

        // --- direction ---
        if (vx > 0) facingRight = true;
        if (vx < 0) facingRight = false;

        // --- animation state (НО НЕ update кадров!) ---
        if (!onGround)      current = jump;
        else if (vx != 0)   current = walk;
        else                current = idle;

        // --- shooting ---
        if (fireCooldown > 0) fireCooldown--;
        if (hitFlashTimer > 0) hitFlashTimer--;
        if (muzzleFlashTimer > 0) muzzleFlashTimer--;

        if (input.isShootPressed() && fireCooldown == 0 && ammo > 0) {
//            shoot(bullets);
            fireCooldown = FIRE_COOLDOWN;
            ammo--;
            input.setShootPressed(false);
        }
    }

    // ================= SHOOT =================

    private void shoot(List<Bullet> bullets) {
        muzzleFlashTimer = 5;

        double dir = facingRight ? 1 : -1;

        double muzzleX = facingRight
                ? x + getWidth() * MUZZLE_X
                : x + getWidth() * (1 - MUZZLE_X);

        double muzzleY = y + getHeight() * MUZZLE_Y;

        bullets.add(new Bullet(
                muzzleX,
                muzzleY,
                dir * BULLET_SPEED
        ));

        vx -= dir * 2.5; // recoil
        SoundManager.play("/sounds/sound-fire.wav");
    }

    // ================= DAMAGE =================

    public void takeDamage(int dmg) {
        hp -= dmg;
        hitFlashTimer = 10;
    }

    // ================= RENDERABLE =================

    @Override
    public void advanceAnimation() {
        current.update();
    }

    @Override
    public boolean isHit() {
        return hitFlashTimer > 0;
    }

    @Override
    public BufferedImage getFrame() {
        return current.getFrame();
    }

    @Override
    public int getDrawX() { return (int) x; }

    @Override
    public int getDrawY() { return (int) y; }

    @Override
    public int getWidth() {
        return (int) (getFrame().getWidth() * SCALE);
    }

    @Override
    public int getHeight() {
        return (int) (getFrame().getHeight() * SCALE);
    }

    @Override
    public boolean isFacingRight() {
        return facingRight;
    }

    // ================= GETTERS =================

    public int getHp() { return hp; }
    public int getMaxHp() { return MAX_HP; }
    public int getAmmo() { return ammo; }
    public boolean isAlive() { return hp > 0; }
    public boolean isShooting() { return muzzleFlashTimer > 0; }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
