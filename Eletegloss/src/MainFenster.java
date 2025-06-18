import javax.swing.*;
import java.awt.*;

public class MainFenster {

    public static void main(String[] args) {
        // System-Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("EliteGloss – Startmenü");
        frame.setSize(440, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        // Frame-Einstellungen

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        // Panel-Einstellungen

        // 🔷 Logo & Titelbereich
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon rawIcon = new ImageIcon("elitegloss_logo_placeholder.png");
            Image img = rawIcon.getImage().getScaledInstance(160, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoLabel.setText("EliteGloss");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
            logoLabel.setForeground(new Color(50, 50, 100));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);
        // Logo wird geladen und skaliert. Wenn das Bild nicht gefunden wird, wird ein Text angezeigt.

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel titel = new JLabel("Willkommen bei EliteGloss");
        titel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titel);
        // Titel wird zentriert und fett dargestellt.
        // Untertitel

        JLabel untertitel = new JLabel("Fahrzeugaufbereitung mit System");
        untertitel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        untertitel.setForeground(Color.GRAY);
        untertitel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(untertitel);
        // Untertitel wird in grauer Schriftart dargestellt.
        // Abstand zwischen Logo und Buttons

        panel.add(Box.createRigidArea(new Dimension(0, 35)));

        // 🟦 Buttons
        JButton btnKunde = createButton("🧍‍♂️ Kunde", "Logge dich als bestehender Kunde ein.");
        JButton btnNeukunde = createButton("🆕 Neukunde registrieren", "Registriere einen neuen Kunden.");
        JButton btnAdmin = createButton("🔐 Adminbereich", "Greife als Admin auf alle Daten zu.");
        btnAdmin.setBackground(new Color(235, 240, 255)); // sanfter Admin-Stil
        btnAdmin.setForeground(new Color(50, 50, 100));
        btnAdmin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnAdmin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdmin.setToolTipText("Nur für autorisierte Benutzer");
        // Buttons werden erstellt. Sie haben Text, Tooltip, Schriftart und Größe.
        // Button im Startmenü

        // Hinzufügen
        panel.add(btnKunde);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnNeukunde);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnAdmin);
        
        // ⚙️ Aktionen
        btnKunde.addActionListener(e -> KundenLogin.zeige());
        btnNeukunde.addActionListener(e -> KundenFormular.zeige());
        btnAdmin.addActionListener(e -> AdminLogin.zeige());
        // e -> bedeutet, dass die Aktion ausgeführt wird, wenn der Button geklickt wird
        // Hier könnten weitere Aktionen hinzugefügt werden

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private static JButton createButton(String text, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        return btn;
        // Ein Button wird erstellt. Er hat Text, Schriftart, Größe und Tooltip.
    }
}
