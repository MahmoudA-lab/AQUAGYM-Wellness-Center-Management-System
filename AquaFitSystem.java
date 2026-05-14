package aquafitsystem;

import javax.swing.SwingUtilities;

public class AquaFitSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardFrame("admin").setVisible(true));
    }
}
