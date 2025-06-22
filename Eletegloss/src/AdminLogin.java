import javax.swing.*;

public class AdminLogin {

    // Hauptmenü (AuswahlFenster) öffnen
    private static void reopenMain() { AuswahlFenster.start(); }

    public static void zeige() {

        while (true) {

            // Passwort-Dialog
            JPasswordField pw = new JPasswordField();
            int res = JOptionPane.showConfirmDialog(
                    null, pw, "Admin-Login (Passwort: admin123)",
                    JOptionPane.OK_CANCEL_OPTION);

            // Abbrechen -> sofort zurück zum Hauptmenü
            if (res != JOptionPane.OK_OPTION) {
                reopenMain();
                return;
            }

            String eingabe = new String(pw.getPassword());

            // korrektes Passwort -> Admin-Bereich öffnen
            if ("admin123".equals(eingabe)) {
                AdminBereich.zeige();   // bleibt offen, bis Fenster geschlossen
                return;
            }

            // falsches Passwort -> Dialog erscheint erneut
            JOptionPane.showMessageDialog(null, "Falsches Passwort!");
            // Schleife läuft einfach weiter
        }
    }
}
