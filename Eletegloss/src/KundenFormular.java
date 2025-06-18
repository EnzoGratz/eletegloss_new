
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class KundenFormular {

    public static void zeige() {
        while (true) {
            JTextField vorname = new JTextField();
            JTextField nachname = new JTextField();
            JTextField telefon = new JTextField();
            JTextField email = new JTextField();
            // Eingabefelder für Vorname, Nachname, Telefonnummer und E-Mail-Adresse
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1, 5, 5));
            panel.setPreferredSize(new Dimension(300, 200));

            panel.add(new JLabel("Vorname:"));
            panel.add(vorname);
            panel.add(new JLabel("Nachname:"));
            panel.add(nachname);
            panel.add(new JLabel("Telefonnummer:"));
            panel.add(telefon);
            panel.add(new JLabel("E-Mail-Adresse:"));
            panel.add(email);
            // Panel mit Eingabefeldern erstellen
            // Eingabefelder für Vorname, Nachname, Telefonnummer und E-Mail-Adresse
            vorname.requestFocus();
            // Fokus auf das Vorname-Feld setzen, damit der Nutzer direkt tippen kann
            int auswahl = JOptionPane.showConfirmDialog(null, panel, "Neukunde registrieren",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (auswahl != JOptionPane.OK_OPTION) break;

            // Eingaben einlesen & trimmen
            String v = vorname.getText().trim();
            String n = nachname.getText().trim();
            String t = telefon.getText().trim();
            String e = email.getText().trim();
            // Eingaben trimmen, um führende/folgende Leerzeichen zu entfernen
            // und um sicherzustellen, dass die Felder nicht leer sind.
            // Eingaben einlesen
            // und trimmen, um führende/folgende Leerzeichen zu entfernen

            // Validierung
            if (v.isEmpty() || n.isEmpty() || t.isEmpty() || e.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Bitte alle Felder ausfüllen.");
                continue;
            }
            // Hier wird überprüft, ob alle Felder ausgefüllt sind.

            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", e)) {
                JOptionPane.showMessageDialog(null, "📧 Ungültige E-Mail-Adresse.");
                continue;
            }
            // Hier wird überprüft, ob die E-Mail-Adresse dem Muster entspricht.
            if (!Pattern.matches("^\\+?[\\d\\s/-]{5,}$", t)) {
                JOptionPane.showMessageDialog(null, "📱 Ungültige Telefonnummer.");
                continue;
            }

            Connection conn = DBVerbindung.verbinde();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "❌ Verbindung zur Datenbank fehlgeschlagen.");
                return;
            }
            // Verbindung zur Datenbank herstellen
            try {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO kunden (vorname, nachname, telefonnummer, email) VALUES (?, ?, ?, ?)");
                stmt.setString(1, v);
                stmt.setString(2, n);
                stmt.setString(3, t);
                stmt.setString(4, e);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "✅ Kunde \"" + v + " " + n + "\" erfolgreich registriert.");
                break;
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "❌ Fehler beim Speichern:\n" + ex.getMessage());
            }
            // Hier werden die Fehler abgefangen, die beim Speichern in die Datenbank auftreten können.
        }
    }
}
