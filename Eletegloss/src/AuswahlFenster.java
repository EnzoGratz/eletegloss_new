import javax.swing.*;
import java.awt.*;

public class AuswahlFenster {

    public static void start() {
        // System-Look übernehmen
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        // Frame
        JFrame f = new JFrame("EliteGloss – Zugang wählen");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout & Rand
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // gleichmäßiger Innenabstand
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0); // Abstand zwischen Zeilen

        // Logo (auf 160 px Breite skalieren)
        ImageIcon raw = new ImageIcon("Logo.png");
        Image     img = raw.getImage().getScaledInstance(160, -1, Image.SCALE_SMOOTH);
        root.add(new JLabel(new ImageIcon(img)), gbc);

        // Titel
        JLabel title = new JLabel("EliteGloss Zugang");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridy = 1;
        root.add(title, gbc);

        // Unterzeile
        JLabel sub = new JLabel("Wähle deinen Zugang aus");
        sub.setFont(sub.getFont().deriveFont(12f));
        gbc.gridy = 2;
        root.add(sub, gbc);

        //Buttons
        Dimension btnSize = new Dimension(180, 32); // einheitliche Breite
        JButton btnKunde    = new JButton("🧍‍♂️  Kunde");     btnKunde.setPreferredSize(btnSize);
        JButton btnNeukunde = new JButton("🆕  Neukunde");   btnNeukunde.setPreferredSize(btnSize);
        JButton btnAdmin    = new JButton("🔐  Admin");      btnAdmin.setPreferredSize(btnSize);

        gbc.gridy = 3; root.add(btnKunde, gbc);
        gbc.gridy = 4; root.add(btnNeukunde, gbc);
        gbc.gridy = 5; root.add(btnAdmin, gbc);

        //Aktionen
        btnKunde.addActionListener(e -> { f.dispose(); KundenLogin.zeige();});
        btnNeukunde.addActionListener(e -> { f.dispose(); KundenFormular.zeige();});
        btnAdmin.addActionListener(e -> { f.dispose(); AdminLogin.zeige();});

        //anzeigen
        f.setContentPane(root);
        f.pack();                 // berechnet perfekte Größe
        f.setLocationRelativeTo(null); // zentriert
        f.setVisible(true);
    }
}
