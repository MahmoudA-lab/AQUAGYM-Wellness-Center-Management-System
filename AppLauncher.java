package aquafitsystem;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("Using default look and feel.");
        }

        UIManager.put("ToolTip.background", Theme.NAVY_CARD);
        UIManager.put("ToolTip.foreground", Theme.TEXT_BRIGHT);
        UIManager.put("OptionPane.background", Theme.NAVY_DARK);
        UIManager.put("OptionPane.messageForeground", Theme.TEXT_BRIGHT);
        UIManager.put("Panel.background", Theme.NAVY_DARK);

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
