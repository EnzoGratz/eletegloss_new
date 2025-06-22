import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Admin‑GUI für EliteGloss.
 * <p>
 * Zeigt alle Kunden und Aufträge aus der Datenbank <code>elitegloss_db</code> an
 * und erlaubt das Filtern nach Auftragsdatum.
 */
public class AdminBereich {

    //ein gemeinsames Tabellen-Objekt; alle Fenster teilen sich dieselbe Datenquelle.
    private static final DefaultTableModel KUNDEN_MODEL = new DefaultTableModel(
            new String[]{"Vorname", "Nachname", "E‑Mail", "Telefon"}, 0) {
        @Override public boolean isCellEditable(int r, int c) {return false;}
    };

    /** Spalten für die Auftragstabelle */
    private static final DefaultTableModel AUFTRAG_MODEL = new DefaultTableModel(
            new String[]{"Kunde", "Datum", "Paket", "Zusatzleistungen", "Preis (€)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) {return false;}
    };

    //nur eine PDF-Liste im ganzen Programm, statt pro Fenster neu.
    private static final DefaultListModel<String> PDF_LIST_MODEL = new DefaultListModel<>();

    private AdminBereich() {

    }
    //Fenster öffnen ohne new AdminBereich()
    //einfach AdminBereich.zeige()
    public static void zeige() {
        JFrame frame = new JFrame("EliteGloss – Adminbereich");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        //Legt fest, was beim Schließen-Klick (X) passiert: Nur dieses Fenster wird „entsorgt“
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override public void windowClosed(java.awt.event.WindowEvent e) {
            AuswahlFenster.start();// genau EIN Aufruf
        }
        });

        //Tabs 
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("📋 Kunden",     new JScrollPane(new JTable(KUNDEN_MODEL)));
        tabs.addTab("📋 Aufträge",    new JScrollPane(new JTable(AUFTRAG_MODEL)));
        tabs.addTab("📋 PDFs",        new JScrollPane(new JList<>(PDF_LIST_MODEL)));

        // Filter Funktion
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField datumField = new JTextField(10);
        JButton filterBtn = new JButton("Nach Datum filtern");
        filterBtn.addActionListener(e -> ladeAuftraegeNachDatum(datumField.getText().trim()));
        filterPanel.add(new JLabel("Datum (YYYY-MM-DD):"));
        filterPanel.add(datumField);
        filterPanel.add(filterBtn);

        frame.add(filterPanel, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);

        // Daten laden
        ladeDaten();
        ladePDFs();

        frame.setVisible(true);
    }

    //Lädt alle Kunden und Aufträge in die Models.

    private static void ladeDaten() {
        KUNDEN_MODEL.setRowCount(0);
        AUFTRAG_MODEL.setRowCount(0);

        try (Connection conn = DBVerbindung.verbinde()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Verbindung zur Datenbank fehlgeschlagen.");
                return; // Macht dann gar nicht weiter mit der Methode. Hört hier auf!
            }

            //Kunden
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT vorname, nachname, email, telefonnummer FROM kunden ORDER BY nachname, vorname")) {

                while (rs.next()) {
                    KUNDEN_MODEL.addRow(new Object[]{
                            rs.getString("vorname"),
                            rs.getString("nachname"),
                            rs.getString("email"),
                            rs.getString("telefonnummer")
                    });
                }
            }

            if (KUNDEN_MODEL.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keine Kunden gefunden.");
            }

            //Aufträge
            final String sql = "SELECT k.vorname, k.nachname, a.datum, a.paket, a.zusatzleistungen, a.preis " +
                    "FROM auftraege a " +
                    "JOIN kunden k ON a.kunde_id = k.id " +
                    "ORDER BY a.datum DESC";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                //In der Klammer, damit das Statement gleich geschlossen wird.
                while (rs.next()) {
                    String kundeName = rs.getString("vorname") + " " + rs.getString("nachname");
                    AUFTRAG_MODEL.addRow(new Object[]{
                            kundeName,
                            rs.getDate("datum").toString(),
                            rs.getString("paket"),
                            rs.getString("zusatzleistungen"),
                            rs.getBigDecimal("preis")
                    });
                }
            }

            if (AUFTRAG_MODEL.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keine Aufträge gefunden.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Daten:\n" + ex.getMessage());
        }
    }

    /**
     * Lädt nur Aufträge eines bestimmten Datums (YYYY-MM-DD) in die Tabelle.
     */
    private static void ladeAuftraegeNachDatum(String datum) {
        AUFTRAG_MODEL.setRowCount(0); // zuerst leeren
        if (datum.isBlank()) {
            ladeDaten(); // wenn leer => alles
            return;
        }
        LocalDate date; //Nur zum Checken angelegt
        try {
            date = LocalDate.parse(datum);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null,"Ungültiges Datum: "+ datum +"\nFormat: YYYY-MM-DD");
            return; //frühzeitiger Ausstieg! Rest von ladeDaten() wird übersprungen
        }

        String sql = "SELECT k.vorname, k.nachname, a.datum, a.paket, a.zusatzleistungen, a.preis " +
                "FROM auftraege a JOIN kunden k ON a.kunde_id = k.id WHERE a.datum = ?";
        try (Connection conn = DBVerbindung.verbinde();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, datum);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AUFTRAG_MODEL.addRow(new Object[]{
                            rs.getString("vorname") + " " + rs.getString("nachname"),
                            rs.getDate("datum").toString(),
                            rs.getString("paket"),
                            rs.getString("zusatzleistungen"),
                            rs.getBigDecimal("preis")
                    });
                }
            }

            if (AUFTRAG_MODEL.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keine Aufträge für " + datum + " gefunden.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Fehler bei der Filterung:\n" + ex.getMessage());
        }
    }

    private static void ladePDFs() {

        PDF_LIST_MODEL.clear();               // Liste leeren

        File pdfDir = new File("PDFs");       // derselbe Ordner wie beim Speichern
        File[] pdfs = pdfDir.listFiles(       // nur *.pdf Dateien
                (dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (pdfs != null) {
            for (File pdf : pdfs) {
                PDF_LIST_MODEL.addElement(pdf.getName()); // Dateiname in die Liste
            }
        }
    }
}
