package aquafitsystem;

import aquafit.db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseCrudPanel extends JPanel {

    private final String title;
    private final String icon;
    private final String tableName;
    private final String[] labels;
    private final String[] dbColumns;
    private final String[] tableHeaders;

    private JTextField[] fields;
    private JTable table;
    private DefaultTableModel model;
    private Object selectedId;

    public DatabaseCrudPanel(String title, String icon, String tableName,
                             String[] labels, String[] dbColumns, String[] tableHeaders) {
        this.title = title;
        this.icon = icon;
        this.tableName = tableName;
        this.labels = labels;
        this.dbColumns = dbColumns;
        this.tableHeaders = tableHeaders;

        setLayout(new BorderLayout(0, 18));
        setBackground(Theme.NAVY_DARK);
        setBorder(BorderFactory.createEmptyBorder(26, 26, 26, 26));

        add(buildHeader(icon + "  " + title), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(18, 0));
        body.setOpaque(false);
        body.add(buildFormCard(), BorderLayout.WEST);

        model = new DefaultTableModel(tableHeaders, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = Theme.darkTable(tableHeaders, new Object[0][0]);
        table.setModel(model);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFieldsFromRow();
        });

        body.add(Theme.darkScroll(table), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        loadData();
    }

    private JPanel buildHeader(String text) {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Theme.TEXT_BRIGHT);

        JPanel underline = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, Theme.AQUA_BRIGHT, 300, 0, new Color(0, 210, 255, 0)));
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

    private JPanel buildFormCard() {
        JPanel card = new JPanel(new BorderLayout(0, 14)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, Theme.NAVY_CARD, 0, getHeight(), Theme.NAVY_MID));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Theme.NAVY_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(340, 0));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 16, 20));

        JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1, 0, 8));
        fieldPanel.setOpaque(false);
        fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new BorderLayout(0, 4));
            row.setOpaque(false);

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setForeground(Theme.AQUA_MID);

            fields[i] = Theme.inputField();
            row.add(label, BorderLayout.NORTH);
            row.add(fields[i], BorderLayout.CENTER);
            fieldPanel.add(row);
        }

        JPanel buttons = new JPanel(new GridLayout(2, 3, 8, 8));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JButton add = crudButton("ADD", "#00D2FF", "#0088CC");
        JButton update = crudButton("UPDATE", "#FF8500", "#CC5500");
        JButton delete = crudButton("DELETE", "#FF4040", "#CC1010");
        JButton clear = crudButton("CLEAR", "#556677", "#334455");
        JButton search = crudButton("SEARCH", "#8855FF", "#5522CC");
        JButton refresh = crudButton("REFRESH", "#00AA77", "#007755");

        add.addActionListener(e -> addRecord());
        update.addActionListener(e -> updateRecord());
        delete.addActionListener(e -> deleteRecord());
        clear.addActionListener(e -> clearFields());
        search.addActionListener(e -> searchRecords());
        refresh.addActionListener(e -> loadData());

        buttons.add(add); buttons.add(update); buttons.add(delete);
        buttons.add(clear); buttons.add(search); buttons.add(refresh);

        card.add(fieldPanel, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);
        return card;
    }

    private JButton crudButton(String text, String c1, String c2) {
        JButton b = new Theme.GradientButton(text, Color.decode(c1), Color.decode(c2));
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setBorder(BorderFactory.createEmptyBorder(9, 6, 9, 6));
        return b;
    }

    private void addRecord() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { show("Database connection failed."); return; }

            StringBuilder cols = new StringBuilder();
            StringBuilder qs = new StringBuilder();
            for (int i = 0; i < dbColumns.length; i++) {
                cols.append(dbColumns[i]);
                qs.append("?");
                if (i < dbColumns.length - 1) { cols.append(", "); qs.append(", "); }
            }

            String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + qs + ")";
            PreparedStatement ps = con.prepareStatement(sql);
            bindFields(ps, 1);
            ps.executeUpdate();

            show("Added successfully.");
            clearFields();
            loadData();
        } catch (Exception e) {
            show("Add Error in " + tableName + ": " + friendlyError(e.getMessage()));
        }
    }

    private void updateRecord() {
        if (selectedId == null) { show("Select a row first."); return; }

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { show("Database connection failed."); return; }
            String idColumn = getFirstColumnName();

            StringBuilder set = new StringBuilder();
            for (int i = 0; i < dbColumns.length; i++) {
                set.append(dbColumns[i]).append("=?");
                if (i < dbColumns.length - 1) set.append(", ");
            }

            String sql = "UPDATE " + tableName + " SET " + set + " WHERE " + idColumn + "=?";
            PreparedStatement ps = con.prepareStatement(sql);
            bindFields(ps, 1);
            ps.setObject(dbColumns.length + 1, selectedId);
            ps.executeUpdate();

            show("Updated successfully.");
            clearFields();
            loadData();
        } catch (Exception e) {
            show("Update Error in " + tableName + ": " + friendlyError(e.getMessage()));
        }
    }

    private void deleteRecord() {
        if (selectedId == null) { show("Select a row first."); return; }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { show("Database connection failed."); return; }
            String idColumn = getFirstColumnName();
            PreparedStatement ps = con.prepareStatement("DELETE FROM " + tableName + " WHERE " + idColumn + "=?");
            ps.setObject(1, selectedId);
            ps.executeUpdate();

            show("Deleted successfully.");
            clearFields();
            loadData();
        } catch (Exception e) {
            show("Delete Error in " + tableName + ": " + friendlyError(e.getMessage()));
        }
    }

    private void searchRecords() {
        String keyword = JOptionPane.showInputDialog(this, "Search:");
        if (keyword == null) return;
        keyword = keyword.trim();

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) { show("Database connection failed."); return; }
            model.setRowCount(0);
            String idColumn = getFirstColumnName();

            StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE CAST(" + idColumn + " AS CHAR) LIKE ?");
            for (String col : dbColumns) sql.append(" OR CAST(").append(col).append(" AS CHAR) LIKE ?");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            String k = "%" + keyword + "%";
            for (int i = 1; i <= dbColumns.length + 1; i++) ps.setString(i, k);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) addRowFromResultSet(rs);
        } catch (Exception e) {
            show("Search Error in " + tableName + ": " + friendlyError(e.getMessage()));
        }
    }

    public void loadData() {
        if (model == null) return;
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return;
            model.setRowCount(0);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " ORDER BY 1");
            while (rs.next()) addRowFromResultSet(rs);
        } catch (Exception e) {
            show("Load Error in " + tableName + ": " + friendlyError(e.getMessage()));
        }
    }

    private void bindFields(PreparedStatement ps, int startIndex) throws SQLException {
        for (int i = 0; i < fields.length; i++) {
            String value = fields[i].getText().trim();
            if (value.isEmpty()) ps.setNull(startIndex + i, Types.NULL);
            else ps.setString(startIndex + i, value);
        }
    }

    private void addRowFromResultSet(ResultSet rs) throws SQLException {
        Object[] row = new Object[tableHeaders.length];
        row[0] = rs.getObject(1);
        for (int i = 0; i < dbColumns.length; i++) row[i + 1] = rs.getObject(dbColumns[i]);
        model.addRow(row);
    }

    private void fillFieldsFromRow() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedId = model.getValueAt(row, 0);
        for (int i = 0; i < fields.length; i++) {
            Object value = model.getValueAt(row, i + 1);
            fields[i].setText(value == null ? "" : value.toString());
        }
    }

    private void clearFields() {
        for (JTextField f : fields) f.setText("");
        selectedId = null;
        table.clearSelection();
    }

    private String getFirstColumnName() throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " LIMIT 0");
            return rs.getMetaData().getColumnName(1);
        }
    }

    private String friendlyError(String msg) {
        if (msg == null) return "Unknown error";
        if (msg.contains("foreign key constraint fails")) {
            return "Foreign key error. Use existing IDs from the related tables first. Example: member_id must exist in members.";
        }
        if (msg.contains("Duplicate entry")) {
            return "Duplicate value. This record already exists or violates a UNIQUE rule.";
        }
        if (msg.contains("cannot be null") || msg.contains("doesn't have a default value")) {
            return "Required field is missing. Fill all NOT NULL fields.";
        }
        return msg;
    }

    private void show(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
