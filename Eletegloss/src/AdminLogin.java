
import javax.swing.*;

public class AdminLogin {
    public static void zeige() {
        JPasswordField pw = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(null, pw, "Admin-Login (Passwort: admin123)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String eingabe = new String(pw.getPassword());
            if (eingabe.equals("admin123")) {
                AdminBereich.zeige();
            } else {
                JOptionPane.showMessageDialog(null, "❌ Falsches Passwort.");
            }
        }
    }
}
// Normaler Admin Login damit man alles sieht