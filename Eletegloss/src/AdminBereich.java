import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class AdminBereich {
    private static DefaultTableModel kundenModel = new DefaultTableModel(new String[]{"Vorname", "Nachname", "E-Mail", "Telefon"}, 0);
    private static DefaultTableModel auftragModel = new DefaultTableModel(new String[]{"Kunde", "Datum", "Kennzeichen", "Marke", "Modell", "Beschreibung"}, 0);
    private static DefaultListModel<String> pdfListModel = new DefaultListModel<>();
    // Datenmodelle für Tabellen und Listen

    public static void zeige() { 
        JFrame frame = new JFrame("EliteGloss – Adminbereich");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        // Frame(Rahmen)-Einstellungen

        JTabbedPane tabs = new JTabbedPane();

        // Kunden-Tabelle
        JTable kundenTabelle = new JTable(kundenModel);
        JScrollPane kundenScroll = new JScrollPane(kundenTabelle);
        tabs.addTab("📋 Kunden", kundenScroll);
        // Kunden-Tabelle

        // Aufträge-Tabelle
        JTable auftragTabelle = new JTable(auftragModel);
        JScrollPane auftragScroll = new JScrollPane(auftragTabelle);
        tabs.addTab("🧾 Aufträge", auftragScroll);
        // Aufträge-Tabelle

        // PDFs
        JList<String> pdfList = new JList<>(pdfListModel);
        JScrollPane pdfScroll = new JScrollPane(pdfList);
        tabs.addTab("📄 PDFs", pdfScroll);
        // PDFs

        // Filter-Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField datumFilter = new JTextField(10);
        JButton filterBtn = new JButton("Nach Datum filtern");
        filterBtn.addActionListener(e -> ladeAuftraegeNachDatum(datumFilter.getText()));
        filterPanel.add(new JLabel("Datum (YYYY-MM-DD):"));
        filterPanel.add(datumFilter);
        filterPanel.add(filterBtn);

        frame.add(tabs, BorderLayout.CENTER);
        frame.add(filterPanel, BorderLayout.NORTH);
        // Filter-Panel
        // Layout-Einstellungen

        // Daten laden
        ladeDaten();
        ladePDFs();

        frame.setVisible(true);
    }

    private static void ladeDaten() {
        kundenModel.setRowCount(0);
        auftragModel.setRowCount(0);

        try (Connection conn = DBVerbindung.verbinde()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Verbindung zur Datenbank fehlgeschlagen.");
                return;
            }

            // Kunden
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM kunden")) {
                while (rs.next()) {
                    kundenModel.addRow(new Object[]{
                        rs.getString("vorname"),
                        rs.getString("nachname"),
                        rs.getString("email"),
                        rs.getString("telefon")
                    });
                }
            }
            //Verbindung zur Datenbank, zeigt ob die Verbindung erfolgreich war
            if (kundenModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keine Kunden gefunden.");
            }
            //Zeigt an, wenn keine Kunden in der Datenbank gefunden wurden
            // Aufträge
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM auftraege")) {
                while (rs.next()) {
                    auftragModel.addRow(new Object[]{
                        rs.getString("kunde"),
                        rs.getString("datum"),
                        rs.getString("kennzeichen"),
                        rs.getString("marke"),
                        rs.getString("modell"),
                        rs.getString("beschreibung")
                    });
                }
            }
            //Man legt alles fest was abgerufen werden soll, in diesem Fall die Aufträge
            if (auftragModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keine Aufträge gefunden.");
            }
            //Zeigt an, wenn keine Aufträge in der Datenbank gefunden wurden
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Daten:\n" + e.getMessage());
        }
    }

    private static void ladeAuftraegeNachDatum(String datum) {
        auftragModel.setRowCount(0);

        try (Connection conn = DBVerbindung.verbinde();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM auftraege WHERE datum = ?")) {
            stmt.setString(1, datum);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                auftragModel.addRow(new Object[]{
                    rs.getString("kunde"),
                    rs.getString("datum"),
                    rs.getString("kennzeichen"),
                    rs.getString("marke"),
                    rs.getString("modell"),
                    rs.getString("beschreibung")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler bei der Filterung:\n" + e.getMessage());
        }
    }
    // Zeigt an wenn ein Fehler beim Laden der Daten auftritt
    private static void ladePDFs() {
        pdfListModel.clear();
        File[] pdfs = new File(".").listFiles((d, name) -> name.toLowerCase().endsWith(".pdf"));
        if (pdfs != null) {
            for (File pdf : pdfs) {
                pdfListModel.addElement(pdf.getName());
            }
        }
    }
}
