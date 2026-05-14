package aquafitsystem;

import java.awt.*;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color panelColor;
    private final boolean showShadow;

    public RoundedPanel(int radius, Color color) { this(radius, color, true); }
    public RoundedPanel(int radius, Color color, boolean showShadow) {
        this.radius = radius;
        this.panelColor = color;
        this.showShadow = showShadow;
        setOpaque(false);
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        if (showShadow) {
            for (int i = 3; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 18 * i));
                g2.fillRoundRect(i, i + 2, w - i * 2, h - i * 2 + 2, radius, radius);
            }
        }
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, w - 6, h - 8, radius, radius);
        g2.setColor(new Color(255, 255, 255, 12));
        g2.fillRoundRect(1, 1, w - 8, Math.max(1, (h - 8) / 2), radius, radius);
        g2.setColor(Theme.NAVY_BORDER);
        g2.drawRoundRect(0, 0, w - 7, h - 9, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
