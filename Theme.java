package aquafitsystem;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Theme {
    public static final Color NAVY_DEEP   = new Color(4, 12, 24);
    public static final Color NAVY_DARK   = new Color(8, 24, 44);
    public static final Color NAVY_MID    = new Color(12, 38, 66);
    public static final Color NAVY_CARD   = new Color(16, 48, 82);
    public static final Color NAVY_BORDER = new Color(28, 70, 110);

    public static final Color AQUA_BRIGHT = new Color(0, 210, 255);
    public static final Color AQUA_MID    = new Color(0, 170, 210);
    public static final Color AQUA_DARK   = new Color(0, 120, 160);

    public static final Color ORANGE_HOT  = new Color(255, 130, 30);
    public static final Color ORANGE_GLOW = new Color(255, 130, 30, 55);

    public static final Color WHITE       = Color.WHITE;
    public static final Color TEXT_BRIGHT = new Color(230, 248, 255);
    public static final Color TEXT_MID    = new Color(160, 210, 230);
    public static final Color TEXT_DIM    = new Color(90, 140, 170);
    public static final Color SUCCESS     = new Color(0, 200, 120);

    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SUBHEAD = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD_SM = new Font("Segoe UI", Font.BOLD, 12);

    public static JTextField inputField() {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(NAVY_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_BRIGHT);
        f.setCaretColor(AQUA_BRIGHT);
        f.setOpaque(false);
        f.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return f;
    }

    public static JTable darkTable(String[] columns, Object[][] rows) {
        JTable t = new JTable(new javax.swing.table.DefaultTableModel(rows, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        t.setFont(FONT_BODY);
        t.setForeground(TEXT_BRIGHT);
        t.setBackground(NAVY_CARD);
        t.setSelectionBackground(new Color(0, 210, 255, 50));
        t.setSelectionForeground(WHITE);
        t.setGridColor(NAVY_BORDER);
        t.setRowHeight(34);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(FONT_BOLD_SM);
        t.getTableHeader().setForeground(AQUA_BRIGHT);
        t.getTableHeader().setBackground(NAVY_MID);
        return t;
    }

    public static JScrollPane darkScroll(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBackground(NAVY_CARD);
        sp.setBorder(BorderFactory.createLineBorder(NAVY_BORDER));
        sp.getViewport().setBackground(NAVY_CARD);
        sp.getVerticalScrollBar().setUI(new AquaScrollBarUI());
        sp.getVerticalScrollBar().setBackground(NAVY_CARD);
        sp.getHorizontalScrollBar().setUI(new AquaScrollBarUI());
        sp.getHorizontalScrollBar().setBackground(NAVY_CARD);
        return sp;
    }

    public static class AquaScrollBarUI extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = AQUA_DARK;
            trackColor = NAVY_MID;
        }
        @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(AQUA_DARK);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }

    public static class GradientButton extends JButton {
        private final Color c1;
        private final Color c2;
        private boolean hovered = false;

        public GradientButton(String text, Color c1, Color c2) {
            super(text);
            this.c1 = c1;
            this.c2 = c2;
            setFont(FONT_SUBHEAD);
            setForeground(WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(11, 24, 11, 24));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color a = hovered ? c1.brighter() : c1;
            Color b = hovered ? c2.brighter() : c2;
            GradientPaint gp = new GradientPaint(0, 0, a, getWidth(), getHeight(), b);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
