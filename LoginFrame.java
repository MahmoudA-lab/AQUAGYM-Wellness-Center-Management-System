package aquafitsystem;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPass;

    public LoginFrame() {
        setTitle("AQUAGYM — Login");
        setSize(1200, 720);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setContentPane(new AnimatedBackground());
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 55, 0, 0);
        row.add(buildBrandPanel(), gbc);
        gbc.gridx = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 40, 0, 55);
        row.add(buildLoginCard(), gbc);
        center.add(row);
        add(center, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildBrandPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        AquaLogo logo = new AquaLogo(3);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setPreferredSize(new Dimension(520, 245));
        JLabel tagline = new JLabel("<html>Train Smarter.<br>Swim Stronger.<br>Live Better.</html>");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 32));
        tagline.setForeground(Theme.TEXT_BRIGHT);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] features = {"● Pool & Gym Access", "● Expert Trainers", "● Physiotherapy", "● Smart Scheduling"};
        JPanel featureList = new JPanel();
        featureList.setLayout(new BoxLayout(featureList, BoxLayout.Y_AXIS));
        featureList.setOpaque(false);
        featureList.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String f : features) {
            JLabel fl = new JLabel(f);
            fl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            fl.setForeground(Theme.TEXT_MID);
            fl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            featureList.add(fl);
        }
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        strip.setOpaque(false);
        strip.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel slogan = new JLabel("  NO LIMITS. JUST RESULTS.  ");
        slogan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        slogan.setForeground(Color.WHITE);
        slogan.setOpaque(true);
        slogan.setBackground(Theme.ORANGE_HOT);
        slogan.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        strip.add(slogan);
        p.add(logo);
        p.add(Box.createVerticalStrut(20));
        p.add(tagline);
        p.add(Box.createVerticalStrut(20));
        p.add(featureList);
        p.add(Box.createVerticalStrut(24));
        p.add(strip);
        return p;
    }

    private JPanel buildLoginCard() {
        GlassCard card = new GlassCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 560));
        card.setBorder(BorderFactory.createEmptyBorder(36, 36, 36, 36));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        AquaLogo smallLogo = new AquaLogo(1);
        smallLogo.setPreferredSize(new Dimension(220, 90));
        JPanel logoWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoWrap.setOpaque(false);
        logoWrap.add(smallLogo);
        g.gridy = 0; g.insets = new Insets(0, 0, 4, 0); card.add(logoWrap, g);
        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Theme.WHITE);
        g.gridy = 1; g.insets = new Insets(4, 0, 4, 0); card.add(title, g);
        JLabel subtitle = new JLabel("Sign in to manage your center", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Theme.TEXT_DIM);
        g.gridy = 2; g.insets = new Insets(0, 0, 24, 0); card.add(subtitle, g);
        card.add(aquaDivider(), withInsets(g, 3, 0, 0, 20, 0));
        card.add(fieldLabel("USERNAME"), withInsets(g, 4, 0, 0, 8, 0));
        usernameField = new JTextField(); styleField(usernameField);
        g.gridy = 5; g.insets = new Insets(0, 0, 14, 0); card.add(usernameField, g);
        card.add(fieldLabel("PASSWORD"), withInsets(g, 6, 0, 0, 8, 0));
        passwordField = new JPasswordField(); stylePasswordField(passwordField);
        g.gridy = 7; g.insets = new Insets(0, 0, 8, 0); card.add(passwordField, g);
        showPass = new JCheckBox("Show password");
        showPass.setOpaque(false); showPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPass.setForeground(Theme.TEXT_DIM); showPass.setFocusPainted(false);
        showPass.addActionListener(e -> passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '●'));
        g.gridy = 8; g.insets = new Insets(0, 0, 22, 0); card.add(showPass, g);
        JButton loginBtn = new Theme.GradientButton("LOGIN →", Theme.AQUA_BRIGHT, Theme.AQUA_MID);
        loginBtn.setPreferredSize(new Dimension(0, 48)); loginBtn.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginBtn);
        g.gridy = 9; g.insets = new Insets(0, 0, 10, 0); card.add(loginBtn, g);
        JButton exitBtn = new Theme.GradientButton("EXIT", Theme.ORANGE_HOT, new Color(200, 80, 10));
        exitBtn.setPreferredSize(new Dimension(0, 42)); exitBtn.addActionListener(e -> System.exit(0));
        g.gridy = 10; g.insets = new Insets(0, 0, 16, 0); card.add(exitBtn, g);
        JLabel hint = new JLabel("Demo  ·  admin / 1234", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12)); hint.setForeground(Theme.TEXT_DIM);
        g.gridy = 11; g.insets = new Insets(0, 0, 0, 0); card.add(hint, g);
        return card;
    }

    private void doLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        if (user.equals("admin") && pass.equals("1234")) {
            dispose();
            new MainDashboardFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "<html><b>Invalid credentials.</b><br><br>Demo login:<br>Username: <b>admin</b><br>Password: <b>1234</b></html>",
                    "Login Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(Theme.AQUA_MID); l.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0)); return l;
    }
    private void styleField(JTextField f) {
        f.setFont(Theme.FONT_BODY); f.setForeground(Theme.TEXT_BRIGHT); f.setCaretColor(Theme.AQUA_BRIGHT); f.setOpaque(false);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.NAVY_BORDER, 1), BorderFactory.createEmptyBorder(10, 14, 10, 14)));
    }
    private void stylePasswordField(JPasswordField f) {
        f.setFont(Theme.FONT_BODY); f.setForeground(Theme.TEXT_BRIGHT); f.setCaretColor(Theme.AQUA_BRIGHT); f.setEchoChar('●'); f.setOpaque(false);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.NAVY_BORDER, 1), BorderFactory.createEmptyBorder(10, 14, 10, 14)));
    }
    private JPanel aquaDivider() {
        JPanel d = new JPanel() { @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); int w = getWidth();
            g2.setPaint(new GradientPaint(0f, 0f, new Color(0, 210, 255, 0), w / 2f, 0f, Theme.AQUA_BRIGHT));
            g2.fillRect(0, 0, w / 2, getHeight());
            g2.setPaint(new GradientPaint(w / 2f, 0f, Theme.AQUA_BRIGHT, w, 0f, new Color(0, 210, 255, 0)));
            g2.fillRect(w / 2, 0, w / 2, getHeight()); g2.dispose(); }};
        d.setPreferredSize(new Dimension(0, 2)); d.setOpaque(false); return d;
    }
    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER)); f.setBackground(new Color(0, 0, 0, 60)); f.setPreferredSize(new Dimension(0, 38));
        JLabel l = new JLabel("© 2026 AQUAGYM Wellness Center  ·  Management System v2.0"); l.setFont(new Font("Segoe UI", Font.PLAIN, 12)); l.setForeground(Theme.TEXT_DIM); f.add(l); return f;
    }
    private GridBagConstraints withInsets(GridBagConstraints base, int row, int t, int l, int b, int r) { base.gridy = row; base.insets = new Insets(t, l, b, r); return base; }

    class AnimatedBackground extends JPanel {
        private float waveOffset = 0;
        AnimatedBackground() { setBackground(Theme.NAVY_DEEP); new javax.swing.Timer(30, e -> { waveOffset += 0.03f; repaint(); }).start(); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g); Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, Theme.NAVY_DEEP, w, h, Theme.NAVY_MID)); g2.fillRect(0, 0, w, h);
            drawWave(g2, w, h, h * 0.82f, h * 0.12f, new Color(0, 150, 200, 35), waveOffset);
            drawWave(g2, w, h, h * 0.87f, h * 0.09f, new Color(0, 190, 240, 25), waveOffset + 1.2f);
            drawWave(g2, w, h, h * 0.92f, h * 0.07f, new Color(0, 210, 255, 15), waveOffset + 2.4f);
            g2.setColor(new Color(255, 255, 255, 6)); int gridSize = 60;
            for (int x = 0; x < w; x += gridSize) g2.drawLine(x, 0, x, h);
            for (int y = 0; y < h; y += gridSize) g2.drawLine(0, y, w, y);
            g2.setColor(new Color(255, 130, 30, 20)); g2.fillOval(w - 320, -120, 460, 460);
            g2.dispose();
        }
        private void drawWave(Graphics2D g2, int w, int h, float baseY, float amplitude, Color color, float offset) {
            GeneralPath wave = new GeneralPath(); wave.moveTo(0, h);
            for (int x = 0; x <= w; x += 4) {
                double y = baseY + Math.sin((x * 0.012) + offset) * amplitude + Math.cos((x * 0.008) + offset * 1.5) * (amplitude * 0.5);
                wave.lineTo(x, y);
            }
            wave.lineTo(w, h); wave.closePath(); g2.setColor(color); g2.fill(wave);
        }
    }
    class GlassCard extends JPanel {
        GlassCard() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            for (int i = 5; i >= 1; i--) { g2.setColor(new Color(0, 0, 0, 14 * i)); g2.fillRoundRect(i + 4, i + 6, w - i * 2, h - i * 2, 28, 28); }
            g2.setPaint(new GradientPaint(0, 0, new Color(10, 30, 52, 235), w, h, new Color(6, 20, 38, 225)));
            g2.fillRoundRect(0, 0, w - 10, h - 12, 28, 28);
            g2.setColor(new Color(255, 255, 255, 10)); g2.fillRoundRect(1, 1, w - 12, 40, 28, 28);
            g2.setColor(new Color(0, 200, 255, 80)); g2.drawRoundRect(0, 0, w - 10, h - 12, 28, 28);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
