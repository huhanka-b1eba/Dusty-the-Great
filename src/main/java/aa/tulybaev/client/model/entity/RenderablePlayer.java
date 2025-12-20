package aa.tulybaev.client.model.entity;

import java.awt.image.BufferedImage;

public interface RenderablePlayer {

    int getDrawX();
    int getDrawY();

    int getWidth();
    int getHeight();

    boolean isFacingRight();
    BufferedImage getFrame();

    boolean isHit();

    // ВАЖНО
    void advanceAnimation();
}

