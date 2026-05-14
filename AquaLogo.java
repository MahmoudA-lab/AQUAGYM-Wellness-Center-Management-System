package aquafitsystem;

import java.awt.*;
import javax.swing.JPanel;

public class AquaLogo extends JPanel {
    private final int size;

    public AquaLogo(int size) {
        this.size = size;
        setOpaque(false);
        setPreferredSize(new Dimension(120 * size, 52 * size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int h = getHeight();
        int dropX = 6;
        int dropY = 5;
        int drop = Math.max(30, (int) (h * 0.58));

        g2.setColor(new Color(0, 210, 255, 35));
        g2.fillOval(dropX - 5, dropY - 5, drop + 10, drop + 10);

        GradientPaint dropPaint = new GradientPaint(dropX, dropY, Theme.AQUA_BRIGHT,
                dropX + drop, dropY + drop, Theme.AQUA_DARK);
        g2.setPaint(dropPaint);
        g2.fillOval(dropX, dropY, drop, drop);

        g2.setColor(new Color(255, 255, 255, 85));
        g2.fillOval(dropX + drop / 4, dropY + drop / 6, drop / 3, drop / 3);

        int textX = dropX + drop + 10;
        int textY = (int) (h * 0.62);
        int fontSize = Math.max(30, (int) (h * 0.58));
        g2.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        g2.setPaint(new GradientPaint(textX, 0, Theme.AQUA_BRIGHT, getWidth(), h, Theme.AQUA_MID));
        g2.drawString("AQUA", textX, textY);

        Font gymFont = new Font("Segoe UI", Font.BOLD, Math.max(12, (int) (h * 0.22)));
        g2.setFont(gymFont);
        FontMetrics fm = g2.getFontMetrics();
        int gymW = fm.stringWidth("GYM");
        int barX = textX;
        int barY = textY + 6;
        int barH = Math.max(16, (int) (h * 0.25));
        g2.setPaint(new GradientPaint(barX, barY, Theme.ORANGE_HOT, barX + gymW + 20, barY, new Color(255, 90, 10)));
        g2.fillRoundRect(barX, barY, gymW + 18, barH, 4, 4);
        g2.setColor(Color.WHITE);
        g2.drawString("GYM", barX + 8, barY + barH - 5);
        g2.dispose();
    }
}
