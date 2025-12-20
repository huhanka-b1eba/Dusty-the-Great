package aa.tulybaev.client.render.components;

import java.awt.*;

public final class HudRenderer {

    public void render(
            Graphics2D g,
            int hp,
            int maxHp,
            int ammo,
            boolean isShooting
    ) {
        drawHpBar(g, hp, maxHp);
        drawAmmo(g, ammo);

        // опционально: индикатор стрельбы
        if (isShooting) {
            drawShootingIndicator(g);
        }
    }

    // ================= HP =================

    private void drawHpBar(Graphics2D g, int hp, int maxHp) {
        int x = 20;
        int y = 20;
        int w = 200;
        int h = 20;

        float hpPercent = Math.max(0f, (float) hp / maxHp);
        int hpWidth = (int) (w * hpPercent);

        // фон
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(x - 2, y - 2, w + 4, h + 4, 10, 10);

        // цвет HP
        if (hpPercent > 0.6f)
            g.setColor(new Color(80, 200, 80));
        else if (hpPercent > 0.3f)
            g.setColor(new Color(240, 200, 70));
        else
            g.setColor(new Color(220, 70, 70));

        g.fillRoundRect(x, y, hpWidth, h, 8, 8);

        // рамка
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, w, h, 8, 8);

        // текст
        g.drawString("HP: " + hp, x + 6, y + 15);
    }

    // ================= AMMO =================

    private void drawAmmo(Graphics2D g, int ammo) {
        int x = 20;
        int y = 50;

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(x - 6, y - 16, 120, 26, 10, 10);

        g.setColor(Color.WHITE);
        g.drawString("Ammo: " + ammo, x, y);
    }

    // ================= EXTRA =================

    private void drawShootingIndicator(Graphics2D g) {
        g.setColor(new Color(255, 220, 120));
        g.drawString("FIRE", 150, 50);
    }
}
