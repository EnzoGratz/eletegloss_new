import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class AuftragErstellen {
    public static void zeige(int kundenId) {
        String[] pakete = {"Basic – 50 €", "Advanced – 80 €", "Ultra – 120 €"};
        JCheckBox motor = new JCheckBox("Motorwäsche (+20 €)");
        JCheckBox innen = new JCheckBox("Innenreinigung (+15 €)");

        JComboBox<String> paketListe = new JComboBox<>(pakete);
        Object[] message = {
            "Paket auswählen:", paketListe,
            "Zusatzleistungen:", motor, innen
        };

        int result = JOptionPane.showConfirmDialog(null, message, "Auftrag erstellen", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            // Auswahl auswerten
            String paket = (String) paketListe.getSelectedItem();
            double preis = 0;
            int anzahl = 1;
            if (paket.contains("Basic")) preis = 50;
            else if (paket.contains("Advanced")) preis = 80;
            else if (paket.contains("Ultra")) preis = 120;

            // Zusatzleistungen
            double motorPreis = motor.isSelected() ? 20 : 0;
            double innenPreis = innen.isSelected() ? 15 : 0;
            double gesamt = preis + motorPreis + innenPreis;

            // Kundendaten laden
            String name = "", nachname = "", email = "";
            Connection conn = DBVerbindung.verbinde();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM kunden WHERE id = ?");
            stmt.setInt(1, kundenId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("vorname");
                nachname = rs.getString("nachname");
                email = rs.getString("email");
            }

            // PDF starten
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA); 
            // Schriftarten initialisieren

            // Logo
            try {
                PDImageXObject logo = PDImageXObject.createFromFile("elitegloss_logo_placeholder.png", doc);
                content.drawImage(logo, 50, 750, 100, 40);
            } catch (Exception ignored) {}

            // Titel
            content.setFont(fontBold, 22);
            content.beginText();
            content.newLineAtOffset(250, 770);
            content.showText("RECHNUNG");
            content.endText();

            // Rechnungsinfos rechts oben
            content.setFont(font, 10);
            int y = 740;
            String rechnungsnummer = "RE-" + (int)(Math.random() * 90000 + 10000);
            String datum = LocalDate.now().toString();

            content.beginText();
            content.newLineAtOffset(400, y);
            content.showText("Rechnungs-Nr.: " + rechnungsnummer);
            content.endText();
            y -= 15;
            content.beginText();
            content.newLineAtOffset(400, y);
            content.showText("Datum: " + datum);
            content.endText();

            // Kundendaten links
            y = 700;
            content.beginText();
            content.setFont(fontBold, 10);
            content.newLineAtOffset(50, y);
            content.showText("Rechnung an:");
            content.endText();
            y -= 15;
            content.setFont(font, 10);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText(name + " " + nachname);
            content.endText();
            y -= 15;
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("E-Mail: " + email);
            content.endText();

            // Tabellenüberschriften
            y = 630;
            content.setFont(fontBold, 11);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Beschreibung");
            content.endText();
            content.beginText();
            content.newLineAtOffset(300, y);
            content.showText("Anzahl");
            content.endText();
            content.beginText();
            content.newLineAtOffset(370, y);
            content.showText("Einzelpreis");
            content.endText();
            content.beginText();
            content.newLineAtOffset(470, y);
            content.showText("Summe");
            content.endText();

            // Tabelleninhalte
            content.setFont(font, 10);
            y -= 20;
            content.beginText(); content.newLineAtOffset(50, y); content.showText(paket); content.endText();
            content.beginText(); content.newLineAtOffset(300, y); content.showText("1"); content.endText();
            content.beginText(); content.newLineAtOffset(370, y); content.showText(String.format("%.2f €", preis)); content.endText();
            content.beginText(); content.newLineAtOffset(470, y); content.showText(String.format("%.2f €", preis)); content.endText();

            if (motor.isSelected()) {
                y -= 15;
                content.beginText(); content.newLineAtOffset(50, y); content.showText("Motorwäsche"); content.endText();
                content.beginText(); content.newLineAtOffset(300, y); content.showText("1"); content.endText();
                content.beginText(); content.newLineAtOffset(370, y); content.showText("20.00 €"); content.endText();
                content.beginText(); content.newLineAtOffset(470, y); content.showText("20.00 €"); content.endText();
            }

            if (innen.isSelected()) {
                y -= 15;
                content.beginText(); content.newLineAtOffset(50, y); content.showText("Innenreinigung"); content.endText();
                content.beginText(); content.newLineAtOffset(300, y); content.showText("1"); content.endText();
                content.beginText(); content.newLineAtOffset(370, y); content.showText("15.00 €"); content.endText();
                content.beginText(); content.newLineAtOffset(470, y); content.showText("15.00 €"); content.endText();
            }

            // Zwischensumme / Summe
            y -= 30;
            content.setFont(fontBold, 10);
            content.beginText(); content.newLineAtOffset(370, y); content.showText("Zwischensumme:"); content.endText();
            content.beginText(); content.newLineAtOffset(470, y); content.showText(String.format("%.2f €", gesamt)); content.endText();

            y -= 15;
            content.beginText(); content.newLineAtOffset(370, y); content.showText("MwSt. (0 %):"); content.endText();
            content.beginText(); content.newLineAtOffset(470, y); content.showText("0.00 €"); content.endText();

            y -= 20;
            content.setFont(fontBold, 12);
            content.beginText(); content.newLineAtOffset(370, y); content.showText("GESAMT:"); content.endText();
            content.beginText(); content.newLineAtOffset(470, y); content.showText(String.format("%.2f €", gesamt)); content.endText();

            // Zahlungsinfo
            y -= 50;
            content.setFont(font, 10);
            content.beginText(); content.newLineAtOffset(50, y); content.showText("Zahlung innerhalb 10 Tage ohne Abzug."); content.endText();
            y -= 15;
            content.beginText(); content.newLineAtOffset(50, y); content.showText("IBAN: AT12 3456 7890 0000 | BIC: ELITATWWXXX"); content.endText();

            // Dank
            y -= 30;
            content.setFont(font, 11);
            content.beginText(); content.newLineAtOffset(50, y); content.showText("Danke für Ihr Vertrauen!"); content.endText();

            content.close();

            // Speichern
            String filename = "Rechnung_" + name + "_" + LocalDate.now() + ".pdf";
            doc.save(new File(filename));
            doc.close();

            JOptionPane.showMessageDialog(null, "✅ PDF erstellt: " + filename);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage());
        }
    }
}