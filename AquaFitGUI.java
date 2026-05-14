//package aquafitsystem;
//
//import aquafit.db.DBConnection;
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.*;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//
///**
// * AquaFitGUI - Standalone quick-access panel (optional).
// * The main entry point is AppLauncher → LoginFrame → MainDashboardFrame.
// */
//public class AquaFitGUI extends JFrame {
//
//    private JTextField txtFirstName, txtLastName, txtDOB, txtEmail, txtPhone, txtAddress;
//    private JTable membersTable;
//    private DefaultTableModel membersModel;
//
//    public AquaFitGUI() {
//        setTitle("AQUAGYM — Members Quick Panel");
//        setSize(1100, 680);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        getContentPane().setBackground(Theme.NAVY_DARK);
//        initUI();
//        loadMembers();
//    }
//
//    private void initUI() {
//        JPanel root = new JPanel(new BorderLayout());
//        root.setBackground(Theme.NAVY_DARK);
//
//        // Header bar
//        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 16)) {
//            @Override protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                GradientPaint gp = new GradientPaint(0, 0, Theme.NAVY_DEEP, getWidth(), 0, Theme.NAVY_MID);
//                g2.setPaint(gp);
//                g2.fillRect(0, 0, getWidth(), getHeight());
//                g2.dispose();
//            }
//        };
//        header.setOpaque(false);
//        header.setPreferredSize(new Dimension(0, 70));
//
//        AquaLogo logo = new AquaLogo(2);
//        logo.setPreferredSize(new Dimension(170, 48));
//        header.add(logo);
//
//        JLabel title = new JLabel("Members Quick Panel");
//        title.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        title.setForeground(Theme.TEXT_MID);
//        header.add(title);
//
//        root.add(header, BorderLayout.NORTH);
//        root.add(buildMembersPanel(), BorderLayout.CENTER);
//        setContentPane(root);
//    }
//
//    private JPanel buildMembersPanel() {
//        JPanel panel = new JPanel(new BorderLayout(16, 16));
//        panel.setBackground(Theme.NAVY_DARK);
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Form card
//        JPanel formCard = new JPanel(new GridLayout(6, 2, 10, 10)) {
//            @Override protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(Theme.NAVY_CARD);
//                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
//                g2.setColor(Theme.NAVY_BORDER);
//                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
//                g2.dispose();
//                super.paintComponent(g);
//            }
//        };
//        formCard.setOpaque(false);
//        formCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        txtFirstName = Theme.inputField();
//        txtLastName  = Theme.inputField();
//        txtDOB       = Theme.inputField();
//        txtEmail     = Theme.inputField();
//        txtPhone     = Theme.inputField();
//        txtAddress   = Theme.inputField();
//
//        String[][] fieldDefs = {
//            {"First Name", ""}, {"Last Name", ""},
//            {"DOB (YYYY-MM-DD)", ""}, {"Email", ""},
//            {"Phone", ""}, {"Address", ""}
//        };
//        JTextField[] fields = {txtFirstName, txtLastName, txtDOB, txtEmail, txtPhone, txtAddress};
//
//        for (int i = 0; i < fieldDefs.length; i++) {
//            JLabel lbl = new JLabel(fieldDefs[i][0]);
//            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
//            lbl.setForeground(Theme.AQUA_MID);
//            formCard.add(lbl);
//            formCard.add(fields[i]);
//        }
//
//        // Buttons
//        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
//        buttons.setOpaque(false);
//        JButton add     = new Theme.GradientButton("ADD",     Theme.AQUA_BRIGHT, Theme.AQUA_MID);
//        JButton refresh = new Theme.GradientButton("REFRESH", Theme.NAVY_CARD, Theme.NAVY_MID);
//        JButton delete  = new Theme.GradientButton("DELETE",  new Color(255,60,60), new Color(180,10,10));
//        JButton clear   = new Theme.GradientButton("CLEAR",   Theme.TEXT_DIM, new Color(60,80,100));
//
//        add.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(Theme.NAVY_BORDER),
//                BorderFactory.createEmptyBorder(9, 18, 9, 18)));
//        refresh.setBorder(add.getBorder());
//        delete.setBorder(add.getBorder());
//        clear.setBorder(add.getBorder());
//
//        add.addActionListener(e     -> addMember());
//        refresh.addActionListener(e -> loadMembers());
//        delete.addActionListener(e  -> deleteMember());
//        clear.addActionListener(e   -> clearFields());
//
//        buttons.add(add);
//        buttons.add(refresh);
//        buttons.add(delete);
//        buttons.add(clear);
//
//        JPanel left = new JPanel(new BorderLayout(0, 12));
//        left.setOpaque(false);
//        left.setPreferredSize(new Dimension(350, 0));
//        left.add(formCard, BorderLayout.CENTER);
//        left.add(buttons, BorderLayout.SOUTH);
//
//        // Table
//        String[] columns = {"ID", "First Name", "Last Name", "DOB", "Email", "Phone", "Address"};
//        membersModel = new DefaultTableModel(columns, 0);
//        membersTable = Theme.darkTable(columns, new Object[0][0]);
//        membersTable.setModel(membersModel);
//
//        panel.add(left, BorderLayout.WEST);
//        panel.add(Theme.darkScroll(membersTable), BorderLayout.CENTER);
//        return panel;
//    }
//
//    private void addMember() {
//        try {
//            Connection con = DBConnection.getConnection();
//            if (con == null) { JOptionPane.showMessageDialog(this, "DB connection failed"); return; }
//            String sql = "INSERT INTO members(first_name,last_name,dob,email,phone,address) VALUES(?,?,?,?,?,?)";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1, txtFirstName.getText());
//            ps.setString(2, txtLastName.getText());
//            ps.setString(3, txtDOB.getText());
//            ps.setString(4, txtEmail.getText());
//            ps.setString(5, txtPhone.getText());
//            ps.setString(6, txtAddress.getText());
//            ps.executeUpdate();
//            JOptionPane.showMessageDialog(this, "✓ Member added successfully");
//            ps.close(); con.close();
//            clearFields(); loadMembers();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }
//    }
//
//    private void loadMembers() {
//        try {
//            Connection con = DBConnection.getConnection();
//            if (con == null) return;
//            membersModel.setRowCount(0);
//            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM members");
//            while (rs.next()) {
//                membersModel.addRow(new Object[]{
//                    rs.getObject(1), rs.getString("first_name"), rs.getString("last_name"),
//                    rs.getString("dob"), rs.getString("email"), rs.getString("phone"), rs.getString("address")
//                });
//            }
//            rs.getStatement().getConnection().close();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }
//    }
//
//    private void deleteMember() {
//        int row = membersTable.getSelectedRow();
//        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a member first"); return; }
//        Object id = membersModel.getValueAt(row, 0);
//        try {
//            Connection con = DBConnection.getConnection();
//            PreparedStatement ps = con.prepareStatement("DELETE FROM members WHERE member_id = ?");
//            ps.setObject(1, id);
//            ps.executeUpdate();
//            JOptionPane.showMessageDialog(this, "✓ Member deleted");
//            ps.close(); con.close();
//            loadMembers();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }
//    }
//
//    private void clearFields() {
//        txtFirstName.setText(""); txtLastName.setText(""); txtDOB.setText("");
//        txtEmail.setText(""); txtPhone.setText(""); txtAddress.setText("");
//    }
//}
