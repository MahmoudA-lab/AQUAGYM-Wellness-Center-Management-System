package aquafitsystem;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class ScaledGifLabel extends JLabel {
    private final ImageIcon icon;
    private final int targetW;
    private final int targetH;

    public ScaledGifLabel(String path, int targetW, int targetH) {
        this.targetW = targetW;
        this.targetH = targetH;
        File f = new File(path);
        icon = f.exists() ? new ImageIcon(path) : null;
        setOpaque(false);
        setPreferredSize(new Dimension(targetW, targetH));
        setMinimumSize(new Dimension(targetW, targetH));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (icon == null) {
            g2.setColor(new Color(0, 210, 255, 35));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(Theme.TEXT_DIM);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("Add images/gym_workout.gif", 14, getHeight() / 2);
            g2.dispose();
            return;
        }

        Image img = icon.getImage();
        int iw = icon.getIconWidth();
        int ih = icon.getIconHeight();
        if (iw <= 0 || ih <= 0) return;

        double scale = Math.min((double) getWidth() / iw, (double) getHeight() / ih);
        int w = (int) (iw * scale);
        int h = (int) (ih * scale);
        int x = getWidth() - w;
        int y = (getHeight() - h) / 2;

        g2.drawImage(img, x, y, w, h, this);
        g2.dispose();
    }
}
