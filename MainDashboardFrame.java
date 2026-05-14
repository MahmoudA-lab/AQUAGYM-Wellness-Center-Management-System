package aquafitsystem;

import aquafit.db.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainDashboardFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String currentUser;
    private JButton activeNavBtn = null;

    public MainDashboardFrame(String currentUser) {
        this.currentUser = currentUser;
        setTitle("AQUAGYM — Management Dashboard");
        setSize(1400, 820);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.NAVY_DEEP);
        root.add(buildSidebar(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.NAVY_DARK);
        main.add(buildTopBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.NAVY_DARK);

        // ── جميع الصفحات ──────────────────────────────────────────────────
        contentPanel.add(buildDashboardPanel(),             "Dashboard");
        contentPanel.add(buildMembersPanel(),               "Members");
        contentPanel.add(buildMembershipPlansPanel(),       "Membership Plans");
        contentPanel.add(buildMemberMembershipsPanel(),     "Member Memberships");
        contentPanel.add(buildTrainersPanel(),              "Trainers");
        contentPanel.add(buildPhysiotherapistsPanel(),      "Physiotherapists");
        contentPanel.add(buildRoomsPanel(),                 "Rooms");
        contentPanel.add(buildPoolsPanel(),                 "Pools");
        contentPanel.add(buildSchedulesPanel(),             "Schedules");
        contentPanel.add(buildClassesPanel(),               "Classes");
        contentPanel.add(buildClassEnrollmentsPanel(),      "Class Enrollments");
        contentPanel.add(buildPersonalTrainingPanel(),      "Personal Training");
        contentPanel.add(buildSwimmingSessionsPanel(),      "Swimming Sessions");
        contentPanel.add(buildPhysiotherapySessionsPanel(), "Physiotherapy Sessions");
        contentPanel.add(buildPaymentsPanel(),              "Payments");
        contentPanel.add(buildAttendancePanel(),            "Attendance");
        contentPanel.add(buildHealthRecordsPanel(),         "Health Records");

        main.add(contentPanel, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

  
    private JPanel buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(230, 0));
        side.setBackground(Theme.NAVY_DEEP);
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.NAVY_BORDER));

        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        logoArea.setBackground(Theme.NAVY_DEEP);
        AquaLogo logo = new AquaLogo(2);
        logo.setPreferredSize(new Dimension(180, 80));
        logoArea.add(logo);

        // ── قائمة التنقل كاملة ────────────────────────────────────────────
        String[][] navItems = {
            {"\u2302", "Dashboard"},
            {"\u2299", "Members"},
            {"\u2606", "Membership Plans"},
            {"\u2605", "Member Memberships"},
            {"\u25B7", "Trainers"},
            {"\u271A", "Physiotherapists"},
            {"\u25A1", "Rooms"},
            {"\u2248", "Pools"},
            {"\u23F0", "Schedules"},
            {"\u25C8", "Classes"},
            {"\u25A3", "Class Enrollments"},
            {"\u25B6", "Personal Training"},
            {"\u2248", "Swimming Sessions"},
            {"\u2764", "Physiotherapy Sessions"},
            {"\u20BF", "Payments"},
            {"\u2713", "Attendance"},
            {"\u2665", "Health Records"}
        };

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(Theme.NAVY_DEEP);
        navPanel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        for (String[] item : navItems) {
            JButton btn = buildNavButton(item[0], item[1]);
            navPanel.add(btn);
            navPanel.add(Box.createVerticalStrut(1));
            if (item[1].equals("Dashboard") && activeNavBtn == null) {
                setActiveNav(btn);
            }
        }

        JScrollPane navScroll = new JScrollPane(navPanel);
        navScroll.setOpaque(false);
        navScroll.getViewport().setOpaque(false);
        navScroll.setBorder(null);
        navScroll.getVerticalScrollBar().setUI(new Theme.AquaScrollBarUI());

        JPanel mid = new JPanel(new BorderLayout());
        mid.setBackground(Theme.NAVY_DEEP);
        mid.add(buildSeparator(), BorderLayout.NORTH);
        mid.add(navScroll, BorderLayout.CENTER);

        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.setBackground(Theme.NAVY_DEEP);
        bottomArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 14, 10));

        JButton logout = new Theme.GradientButton("LOGOUT", Theme.ORANGE_HOT, new Color(180, 60, 10));
        logout.setPreferredSize(new Dimension(0, 40));
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        JLabel ver = new JLabel("v2.0 - AQUAGYM Pro", SwingConstants.CENTER);
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ver.setForeground(Theme.TEXT_DIM);

        bottomArea.add(logout, BorderLayout.CENTER);
        bottomArea.add(ver, BorderLayout.SOUTH);

        side.add(logoArea, BorderLayout.NORTH);
        side.add(mid, BorderLayout.CENTER);
        side.add(bottomArea, BorderLayout.SOUTH);
        return side;
    }

    private JPanel buildSeparator() {
        JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth();
                float[] fractions = {0.0f, 0.5f, 1.0f};
                Color[] colors = {
                    new Color(0, 210, 255, 0),
                    Theme.AQUA_DARK,
                    new Color(0, 210, 255, 0)
                };
                LinearGradientPaint lgp = new LinearGradientPaint(
                        0f, 0f, (float) w, 0f, fractions, colors);
                g2.setPaint(lgp);
                g2.fillRect(0, 0, w, getHeight());
                g2.dispose();
            }
        };
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setOpaque(false);
        sep.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        return sep;
    }

    private JButton buildNavButton(String icon, String page) {
        JButton b = new JButton(icon + "  " + page) {
            boolean active = false;
            {
                addActionListener(e -> {
                    cardLayout.show(contentPanel, page);
                    setActiveNav(this);
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { repaint(); }
                    public void mouseExited(MouseEvent e)  { repaint(); }
                });
            }
            public void setActive(boolean a) { this.active = a; repaint(); }
            public boolean isActive() { return active; }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    GradientPaint gp = new GradientPaint(
                            0f, 0f, new Color(0, 190, 240, 55),
                            (float) getWidth(), 0f, new Color(0, 190, 240, 10));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(Theme.AQUA_BRIGHT);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(0, 5, 0, getHeight() - 5);
                    setForeground(Theme.AQUA_BRIGHT);
                } else {
                    setForeground(Theme.TEXT_MID);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 10));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return b;
    }

    @SuppressWarnings("unchecked")
    private void setActiveNav(JButton btn) {
        if (activeNavBtn != null) {
            try {
                activeNavBtn.getClass().getMethod("setActive", boolean.class)
                        .invoke(activeNavBtn, false);
            } catch (Exception ignored) {}
        }
        try {
            btn.getClass().getMethod("setActive", boolean.class).invoke(btn, true);
        } catch (Exception ignored) {}
        activeNavBtn = btn;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, 62));
        bar.setBackground(Theme.NAVY_MID);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.NAVY_BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)));

        JLabel title = new JLabel("AQUAGYM - Wellness Center Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Theme.TEXT_BRIGHT);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(pill("ONLINE", Theme.SUCCESS));
        right.add(pill(currentUser.toUpperCase(), Theme.AQUA_DARK));

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

   
    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(Theme.NAVY_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        p.add(buildHeroBanner(), BorderLayout.NORTH);

        // 12 dashboard cards so all important database sections appear.
        JPanel cardsGrid = new JPanel(new GridLayout(3, 4, 12, 12));
        cardsGrid.setOpaque(false);

        JPanel[] cards = new JPanel[12];
        cards[0]  = statCard("MEMBERS",      "...", "Registered",        Theme.AQUA_BRIGHT);
        cards[1]  = statCard("PLANS",        "...", "Active Plans",      new Color(100, 200, 100));
        cards[2]  = statCard("TRAINERS",     "...", "Expert Staff",      Theme.ORANGE_HOT);
        cards[3]  = statCard("PHYSIOS",      "...", "Therapists",        new Color(180, 100, 255));
        cards[4]  = statCard("ROOMS",        "...", "Gym Rooms",         new Color(255, 200, 0));
        cards[5]  = statCard("POOLS",        "...", "Swimming",          Theme.AQUA_BRIGHT);
        cards[6]  = statCard("CLASSES",      "...", "Fitness Classes",   new Color(255, 200, 0));
        cards[7]  = statCard("SESSIONS",     "...", "Booked",            Theme.AQUA_MID);
        cards[8]  = statCard("ENROLLMENTS",  "...", "Class Enrollments", new Color(140, 100, 255));
        cards[9]  = statCard("MEMBERSHIPS",  "...", "Member Plans",      new Color(70, 220, 120));
        cards[10] = statCard("REVENUE",      "...", "Total Revenue",     new Color(0, 220, 140));
        cards[11] = statCard("ATTENDANCE",   "...", "Today",             Theme.ORANGE_HOT);

        for (JPanel c : cards) {
            cardsGrid.add(c);
        }

        loadDashboardStats(cards);

        // GIF is placed in the bottom-right corner, not in the hero banner.
        JPanel bottomRow = new JPanel(new BorderLayout(14, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(buildQuickActions(), BorderLayout.WEST);
        bottomRow.add(buildGifPanel(), BorderLayout.EAST);

        JPanel centerWrap = new JPanel(new BorderLayout(0, 12));
        centerWrap.setOpaque(false);
        centerWrap.add(cardsGrid, BorderLayout.CENTER);
        centerWrap.add(bottomRow, BorderLayout.SOUTH);

        p.add(centerWrap, BorderLayout.CENTER);
        return p;
    }

    private void loadDashboardStats(JPanel[] cards) {
        Connection con = DBConnection.getConnection();
        if (con == null) return;

        String[] queries = {
            "SELECT COUNT(*) FROM members",
            "SELECT COUNT(*) FROM membership_plans",
            "SELECT COUNT(*) FROM trainers",
            "SELECT COUNT(*) FROM physiotherapists",
            "SELECT COUNT(*) FROM rooms",
            "SELECT COUNT(*) FROM pools",
            "SELECT COUNT(*) FROM fitness_classes",
            "SELECT (SELECT COUNT(*) FROM personal_training_sessions) + " +
            "       (SELECT COUNT(*) FROM swimming_sessions) + " +
            "       (SELECT COUNT(*) FROM physiotherapy_sessions)",
            "SELECT COUNT(*) FROM class_enrollments",
            "SELECT COUNT(*) FROM member_memberships",
            "SELECT COALESCE(SUM(amount),0) FROM payments",
            "SELECT COUNT(*) FROM attendance WHERE attend_date = CURDATE()"
        };

        try {
            for (int i = 0; i < queries.length && i < cards.length; i++) {
                try (Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery(queries[i])) {
                    if (rs.next()) {
                        String val = (i == 10)
                                ? String.format("$%.0f", rs.getDouble(1))
                                : String.valueOf(rs.getInt(1));
                        updateCardValue(cards[i], val);
                    }
                } catch (Exception ignored) {}
            }
            con.close();
        } catch (Exception ignored) {}
    }

    private void updateCardValue(JPanel card, String val) {
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getFont().getSize() >= 28) {
                ((JLabel) c).setText(val);
                return;
            }
        }
    }

    private JPanel buildGifPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(140, 86));

        File gifFile = new File("images/gym_workout.gif");
        if (gifFile.exists()) {
            ImageIcon gifIcon = new ImageIcon(gifFile.getAbsolutePath());

            // Small and proportional, positioned bottom-right beside the quick buttons.
            Image scaled = gifIcon.getImage().getScaledInstance(118, 76, Image.SCALE_DEFAULT);
            JLabel gifLabel = new JLabel(new ImageIcon(scaled));
            gifLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            gifLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            gifLabel.setOpaque(false);
            gifLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.AQUA_DARK, 1),
                    BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            panel.add(gifLabel, BorderLayout.SOUTH);
        } else {
            JLabel fallback = new JLabel("GIF", SwingConstants.CENTER);
            fallback.setFont(new Font("Segoe UI", Font.BOLD, 12));
            fallback.setForeground(Theme.AQUA_BRIGHT);
            fallback.setPreferredSize(new Dimension(118, 76));
            fallback.setBorder(BorderFactory.createLineBorder(Theme.NAVY_BORDER));
            panel.add(fallback, BorderLayout.SOUTH);
        }
        return panel;
    }

    private JPanel buildHeroBanner() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0f, 0f, Theme.NAVY_MID,
                        (float) getWidth(), 0f, new Color(20, 55, 80));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(Theme.AQUA_BRIGHT);
                g2.fillRoundRect(0, getHeight() - 4, getWidth(), 4, 2, 2);
                g2.setColor(new Color(0, 210, 255, 18));
                g2.fillOval(getWidth() - 200, -50, 280, 280);
                g2.setColor(Theme.ORANGE_GLOW);
                g2.fillOval(getWidth() - 120, 10, 120, 120);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        hero.setOpaque(false);
        hero.setPreferredSize(new Dimension(0, 95));
        hero.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel welcome = new JLabel("Welcome back, " + currentUser.toUpperCase());
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setForeground(Theme.WHITE);

        JLabel sub = new JLabel("Complete Gym, Swimming, Physiotherapy, Membership and Payment Management");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Theme.TEXT_MID);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);
        text.add(welcome);
        text.add(Box.createVerticalStrut(6));
        text.add(sub);
        hero.add(text, BorderLayout.WEST);
        return hero;
    }

    private JPanel statCard(String label, String value, String sub, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0f, 0f, Theme.NAVY_CARD,
                        (float) getWidth(), (float) getHeight(), Theme.NAVY_MID);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.setColor(Theme.NAVY_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Theme.TEXT_DIM);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 32));
        val.setForeground(accent);

        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(Theme.TEXT_MID);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        card.add(subLbl, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildQuickActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setOpaque(false);
        JLabel lbl = new JLabel("Quick:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Theme.TEXT_MID);
        p.add(lbl);

        JButton addMember = quickButton("+ Add Member");
        addMember.addActionListener(e -> cardLayout.show(contentPanel, "Members"));

        JButton newSession = quickButton("+ New Session");
        newSession.addActionListener(e -> cardLayout.show(contentPanel, "Personal Training"));

        JButton swimming = quickButton("Swimming");
        swimming.addActionListener(e -> cardLayout.show(contentPanel, "Swimming Sessions"));

        JButton payments = quickButton("Payments");
        payments.addActionListener(e -> cardLayout.show(contentPanel, "Payments"));

        JButton attendance = quickButton("Attendance");
        attendance.addActionListener(e -> cardLayout.show(contentPanel, "Attendance"));

        p.add(addMember);
        p.add(newSession);
        p.add(swimming);
        p.add(payments);
        p.add(attendance);
        return p;
    }

    private JButton quickButton(String text) {
        JButton b = new Theme.GradientButton(text, Theme.NAVY_CARD, Theme.NAVY_MID);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.NAVY_BORDER),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        return b;
    }

  
    private JPanel buildMembersPanel() {
        return buildCrudPanel("Members", "\u2299",
            new String[]{"First Name","Last Name","DOB (YYYY-MM-DD)","Email","Phone","Address"},
            new String[]{"ID","First Name","Last Name","DOB","Email","Phone","Address"},
            "members", "member_id",
            new String[]{"first_name","last_name","dob","email","phone","address"});
    }

    private JPanel buildMembershipPlansPanel() {
        return buildCrudPanel("Membership Plans", "\u2606",
            new String[]{"Plan Name","Duration (Months)","Price ($)"},
            new String[]{"ID","Plan Name","Duration","Price"},
            "membership_plans", "membership_id",
            new String[]{"name","duration_months","price"});
    }

    private JPanel buildMemberMembershipsPanel() {
        return buildCrudPanel("Member Memberships", "\u2605",
            new String[]{"Member ID","Membership ID","Start Date","End Date","Status"},
            new String[]{"ID","Member ID","Membership ID","Start Date","End Date","Status"},
            "member_memberships", "member_membership_id",
            new String[]{"member_id","membership_id","start_date","end_date","status"});
    }

    private JPanel buildTrainersPanel() {
        return buildCrudPanel("Trainers", "\u25B7",
            new String[]{"First Name","Last Name","Specialty","Email","Phone"},
            new String[]{"ID","First Name","Last Name","Specialty","Email","Phone"},
            "trainers", "trainer_id",
            new String[]{"first_name","last_name","specialty","email","phone"});
    }

    private JPanel buildPhysiotherapistsPanel() {
        return buildCrudPanel("Physiotherapists", "\u271A",
            new String[]{"First Name","Last Name","Specialty","Email","Phone"},
            new String[]{"ID","First Name","Last Name","Specialty","Email","Phone"},
            "physiotherapists", "physio_id",
            new String[]{"first_name","last_name","specialty","email","phone"});
    }

    private JPanel buildRoomsPanel() {
        return buildCrudPanel("Rooms", "\u25A1",
            new String[]{"Room Name","Room Type","Capacity"},
            new String[]{"ID","Name","Type","Capacity"},
            "rooms", "room_id",
            new String[]{"name","room_type","capacity"});
    }

    private JPanel buildPoolsPanel() {
        return buildCrudPanel("Pools", "\u2248",
            new String[]{"Pool Name","Location","Capacity"},
            new String[]{"ID","Name","Location","Capacity"},
            "pools", "pool_id",
            new String[]{"name","location","capacity"});
    }

    private JPanel buildSchedulesPanel() {
        return buildCrudPanel("Schedules", "\u23F0",
            new String[]{"Day of Week","Start Time (HH:MM:SS)","End Time (HH:MM:SS)"},
            new String[]{"ID","Day","Start Time","End Time"},
            "schedules", "schedule_id",
            new String[]{"day_of_week","start_time","end_time"});
    }

    private JPanel buildClassesPanel() {
        return buildCrudPanel("Fitness Classes", "\u25C8",
            new String[]{"Class Name","Schedule ID","Room ID","Trainer ID","Max Capacity"},
            new String[]{"ID","Class Name","Schedule ID","Room ID","Trainer ID","Capacity"},
            "fitness_classes", "class_id",
            new String[]{"class_name","schedule_id","room_id","trainer_id","max_capacity"});
    }

    private JPanel buildClassEnrollmentsPanel() {
        return buildCrudPanel("Class Enrollments", "\u25A3",
            new String[]{"Member ID","Class ID","Enrollment Date","Status"},
            new String[]{"ID","Member ID","Class ID","Date","Status"},
            "class_enrollments", "enrollment_id",
            new String[]{"member_id","class_id","enrollment_date","status"});
    }

    private JPanel buildPersonalTrainingPanel() {
        return buildCrudPanel("Personal Training", "\u25B6",
            new String[]{"Member ID","Trainer ID","Room ID","Date (YYYY-MM-DD)","Start Time","End Time","Status"},
            new String[]{"ID","Member","Trainer","Room","Date","Start","End","Status"},
            "personal_training_sessions", "psession_id",
            new String[]{"member_id","trainer_id","room_id","session_date","start_time","end_time","status"});
    }

    private JPanel buildSwimmingSessionsPanel() {
        return buildCrudPanel("Swimming Sessions", "\u2248",
            new String[]{"Member ID","Pool ID","Trainer ID","Date (YYYY-MM-DD)","Start Time","End Time","Status"},
            new String[]{"ID","Member","Pool","Trainer","Date","Start","End","Status"},
            "swimming_sessions", "swim_session_id",
            new String[]{"member_id","pool_id","trainer_id","session_date","start_time","end_time","status"});
    }

    private JPanel buildPhysiotherapySessionsPanel() {
        return buildCrudPanel("Physiotherapy Sessions", "\u2764",
            new String[]{"Member ID","Physio ID","Room ID","Date (YYYY-MM-DD)","Start Time","End Time","Status"},
            new String[]{"ID","Member","Physio","Room","Date","Start","End","Status"},
            "physiotherapy_sessions", "physio_session_id",
            new String[]{"member_id","physio_id","room_id","session_date","start_time","end_time","status"});
    }

    private JPanel buildPaymentsPanel() {
        return buildCrudPanel("Payments", "\u20BF",
            new String[]{"Member ID","Membership ID","P.Session ID","Swim ID","Physio ID","Amount","Date","Method"},
            new String[]{"ID","Member","Membership","P.Session","Swim","Physio","Amount","Date","Method"},
            "payments", "payment_id",
            new String[]{"member_id","member_membership_id","psession_id","swim_session_id",
                         "physio_session_id","amount","payment_date","payment_method"});
    }

    private JPanel buildAttendancePanel() {
        return buildCrudPanel("Attendance", "\u2713",
            new String[]{"Member ID","Date (YYYY-MM-DD)","Check-In Time","Check-Out Time","Status"},
            new String[]{"ID","Member ID","Date","Check In","Check Out","Status"},
            "attendance", "attendance_id",
            new String[]{"member_id","attend_date","check_in_time","check_out_time","status"});
    }

    private JPanel buildHealthRecordsPanel() {
        return buildCrudPanel("Health Records", "\u2665",
            new String[]{"Member ID","Condition","Notes","Record Date"},
            new String[]{"ID","Member","Condition","Notes","Date"},
            "health_records", "health_id",
            new String[]{"member_id","condition_name","notes","record_date"});
    }

  
    private JPanel buildCrudPanel(String title, String icon,
                                  String[] formLabels, String[] tableColumns,
                                  String dbTable, String idColumn, String[] dbCols) {

        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Theme.NAVY_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        panel.add(buildSectionHeader(icon + "  " + title), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(tableColumns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = Theme.darkTable(tableColumns, new Object[0][0]);
        table.setModel(model);

        JTextField[] fields = new JTextField[formLabels.length];
        JPanel formCard = buildFormCard(formLabels, fields);
        JPanel buttons  = buildDbButtons(dbTable, idColumn, dbCols, fields, model, table);

        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(310, 0));
        left.add(formCard, BorderLayout.CENTER);
        left.add(buttons, BorderLayout.SOUTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setOpaque(false);
        body.add(left, BorderLayout.WEST);
        body.add(Theme.darkScroll(table), BorderLayout.CENTER);
        panel.add(body, BorderLayout.CENTER);

        loadTableData(model, dbTable);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                for (int i = 0; i < fields.length && (i + 1) < model.getColumnCount(); i++) {
                    Object val = model.getValueAt(row, i + 1);
                    fields[i].setText(val != null ? val.toString() : "");
                }
            }
        });

        return panel;
    }

 
    private JPanel buildFormCard(String[] labels, JTextField[] fields) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0f, 0f, Theme.NAVY_CARD,
                        0f, (float) getHeight(), Theme.NAVY_MID);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Theme.NAVY_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 14, 18));

        JPanel fieldsPanel = new JPanel(new GridLayout(labels.length, 1, 0, 7));
        fieldsPanel.setOpaque(false);

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new BorderLayout(0, 3));
            row.setOpaque(false);
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(Theme.AQUA_MID);
            fields[i] = Theme.inputField();
            row.add(lbl, BorderLayout.NORTH);
            row.add(fields[i], BorderLayout.CENTER);
            fieldsPanel.add(row);
        }
        card.add(fieldsPanel, BorderLayout.CENTER);
        return card;
    }

    
    private JPanel buildDbButtons(String dbTable, String idColumn, String[] dbCols,
                                  JTextField[] fields, DefaultTableModel model, JTable table) {
        JPanel buttons = new JPanel(new GridLayout(2, 3, 7, 7));
        buttons.setOpaque(false);

      
        JButton add = makeBtn("ADD", "#00D2FF", "#0088CC");
        add.addActionListener(e -> {
            for (JTextField f : fields) {
                if (f.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            Connection con = DBConnection.getConnection();
            if (con == null) { showDbError(); return; }
            try {
                StringBuilder sql = new StringBuilder("INSERT INTO " + dbTable + " (");
                for (int i = 0; i < dbCols.length; i++)
                    sql.append(dbCols[i]).append(i < dbCols.length - 1 ? "," : ") VALUES (");
                for (int i = 0; i < dbCols.length; i++)
                    sql.append("?").append(i < dbCols.length - 1 ? "," : ")");
                PreparedStatement ps = con.prepareStatement(sql.toString());
                for (int i = 0; i < fields.length; i++)
                    ps.setString(i + 1, fields[i].getText().trim());
                ps.executeUpdate();
                ps.close(); con.close();
                JOptionPane.showMessageDialog(this, "Added successfully!", "AQUAGYM",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(fields);
                loadTableData(model, dbTable);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

       
        JButton update = makeBtn("UPDATE", "#FF8500", "#CC5500");
        update.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row first!"); return; }
            Object id = model.getValueAt(row, 0);
            Connection con = DBConnection.getConnection();
            if (con == null) { showDbError(); return; }
            try {
                StringBuilder sql = new StringBuilder("UPDATE " + dbTable + " SET ");
                for (int i = 0; i < dbCols.length; i++)
                    sql.append(dbCols[i]).append("=?").append(i < dbCols.length - 1 ? "," : "");
                sql.append(" WHERE ").append(idColumn).append("=?");
                PreparedStatement ps = con.prepareStatement(sql.toString());
                for (int i = 0; i < fields.length; i++)
                    ps.setString(i + 1, fields[i].getText().trim());
                ps.setObject(fields.length + 1, id);
                ps.executeUpdate();
                ps.close(); con.close();
                JOptionPane.showMessageDialog(this, "Updated successfully!", "AQUAGYM",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(fields);
                loadTableData(model, dbTable);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        JButton delete = makeBtn("DELETE", "#FF4040", "#CC1010");
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row first!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            Object id = model.getValueAt(row, 0);
            Connection con = DBConnection.getConnection();
            if (con == null) { showDbError(); return; }
            try {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM " + dbTable + " WHERE " + idColumn + "=?");
                ps.setObject(1, id);
                ps.executeUpdate();
                ps.close(); con.close();
                JOptionPane.showMessageDialog(this, "Deleted successfully!", "AQUAGYM",
                        JOptionPane.INFORMATION_MESSAGE);
                clearFields(fields);
                loadTableData(model, dbTable);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

       
        JButton clear = makeBtn("CLEAR", "#556677", "#334455");
        clear.addActionListener(e -> { clearFields(fields); table.clearSelection(); });

        
        JButton search = makeBtn("SEARCH", "#8855FF", "#5522CC");
        search.addActionListener(e -> {
            String keyword = fields[0].getText().trim();
            if (keyword.isEmpty()) { loadTableData(model, dbTable); return; }
            Connection con = DBConnection.getConnection();
            if (con == null) { showDbError(); return; }
            try {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM " + dbTable + " WHERE " + dbCols[0] + " LIKE ?");
                ps.setString(1, "%" + keyword + "%");
                ResultSet rs = ps.executeQuery();
                model.setRowCount(0);
                int cols = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] rowData = new Object[cols];
                    for (int i = 0; i < cols; i++) rowData[i] = rs.getObject(i + 1);
                    model.addRow(rowData);
                }
                rs.close(); ps.close(); con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

       
        JButton refresh = makeBtn("REFRESH", "#00AA77", "#007755");
        refresh.addActionListener(e -> { clearFields(fields); loadTableData(model, dbTable); });

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);
        buttons.add(search);
        buttons.add(refresh);
        return buttons;
    }

  
    private JButton makeBtn(String text, String c1hex, String c2hex) {
        JButton b = new Theme.GradientButton(text, Color.decode(c1hex), Color.decode(c2hex));
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        return b;
    }

    private void loadTableData(DefaultTableModel model, String dbTable) {
        model.setRowCount(0);
        Connection con = DBConnection.getConnection();
        if (con == null) return;
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + dbTable);
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }
            rs.close(); con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage());
        }
    }

    private void clearFields(JTextField[] fields) {
        for (JTextField f : fields) f.setText("");
    }

    private void showDbError() {
        JOptionPane.showMessageDialog(this,
                "Database connection failed!\nCheck DBConnection settings.",
                "DB Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel buildSectionHeader(String title) {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(Theme.TEXT_BRIGHT);

        JPanel underline = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(
                        0f, 0f, Theme.AQUA_BRIGHT, 300f, 0f,
                        new Color(0, 210, 255, 0));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        underline.setPreferredSize(new Dimension(0, 3));
        underline.setOpaque(false);

        h.add(lbl, BorderLayout.NORTH);
        h.add(underline, BorderLayout.SOUTH);
        return h;
    }

    private JLabel pill(String text, Color bg) {
        JLabel l = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(bg);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(bg);
        l.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        l.setOpaque(false);
        return l;
    }
}