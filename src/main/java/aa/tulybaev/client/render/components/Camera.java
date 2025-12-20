package aa.tulybaev.client.render.components;

import aa.tulybaev.client.model.entity.RenderablePlayer;

public final class Camera {

    private int x, y;

    public void follow(RenderablePlayer target, int screenW, int screenH) {
        x = target.getDrawX() - screenW / 2;
        y = target.getDrawY() - screenH / 2;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
