import javax.swing.*;
import java.awt.*;

public class AuswahlFenster {

    public static void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Logo
        ImageIcon icon = null;
        try {
            icon = new ImageIcon("elitegloss_logo_placeholder.png");
        } catch (Exception ignored) {}

        // HTML-Nachricht
        String nachricht = """
            <html>
                <div style='text-align:center; font-family:SansSerif;'>
                    <h2 style='color:#2E86C1; margin-bottom:5px;'>EliteGloss Zugang</h2>
                    <p style='font-size:13px; color:#555555;'>Wähle deinen Zugang aus und starte durch.</p>
                </div>
            </html>
            """;

        // Buttons mit Icons
        Object[] optionen = {
                "🧍‍♂️  Kunde",
                "🆕  Neukunde",
                "🔐  Admin"
        };

        int auswahl = JOptionPane.showOptionDialog(
                null,
                nachricht,
                "EliteGloss – Zugang wählen",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon,
                optionen,
                optionen[0]
        );

        // Aktionen basierend auf Auswahl
        switch (auswahl) {
            case 0 -> KundenLogin.zeige();
            case 1 -> KundenFormular.zeige();
            case 2 -> AdminLogin.zeige();
            default -> {} // Fenster geschlossen
        }
    }
}
