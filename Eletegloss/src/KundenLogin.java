
import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class KundenLogin {

    public static void zeige() {
        Connection conn = DBVerbindung.verbinde();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "❌ Verbindung zur Datenbank fehlgeschlagen.");
            return;
        }
        // Lade Kunden aus der Datenbank
        // und zeige sie in einem Auswahlfeld an.
        DefaultComboBoxModel<String> kundenModel = new DefaultComboBoxModel<>();
        kundenModel.addElement("Bitte wählen ...");

        try (
            PreparedStatement stmt = conn.prepareStatement("SELECT id, vorname, nachname FROM kunden ORDER BY id");
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("vorname") + " " + rs.getString("nachname");
                kundenModel.addElement(id + " – " + name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Kunden:\n" + e.getMessage());
            return;
        }

        JComboBox<String> kundenBox = new JComboBox<>(kundenModel);
        kundenBox.setPreferredSize(new Dimension(280, 28));
        kundenBox.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Bitte Kunde auswählen:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(kundenBox);

        int auswahl = JOptionPane.showConfirmDialog(null, panel, "🧍‍♂️ Kundenlogin",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        String selected = (String) kundenBox.getSelectedItem();

        if (auswahl == JOptionPane.OK_OPTION &&
            selected != null && !selected.startsWith("Bitte wählen")) {

            try {
                int kundenId = Integer.parseInt(selected.split(" – ")[0]);
                AuftragErstellen.zeige(kundenId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Fehlerhafte Kundenauswahl.");
            }
        }// Wenn der Nutzer auf OK klickt und einen gültigen Kunden ausgewählt hat,
    }
}
