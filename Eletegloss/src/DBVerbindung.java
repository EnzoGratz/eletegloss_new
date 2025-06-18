import java.sql.Connection;
import java.sql.DriverManager;

public class DBVerbindung {
    // Passe hier die Datenbank-URL, den Benutzer und das Passwort an deine Umgebung an!
    private static final String URL = "jdbc:mysql://localhost:3306/elitegloss_db";
    private static final String USER = "root";      // ggf. anpassen
    private static final String PASS = "";          // ggf. anpassen

    public static Connection verbinde() {
        try {
            // Bei modernen JDBC-Treibern ist das Laden der Klasse meist nicht mehr nötig,
            // aber es schadet nicht:
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            return conn;
        } catch (Exception e) {
            System.err.println("❌ Fehler bei der Datenbankverbindung:");
            e.printStackTrace();
            return null;
        }
    }
}
// Diese Klasse stellt die Verbindung zur MySQL-Datenbank her.
// Stelle sicher, dass der MySQL Connector/J im Classpath ist.
// Die URL, der Benutzer und das Passwort müssen an deine Datenbank angepasst werden.
// Diese Klasse ist für die Verbindung zur Datenbank zuständig.
// Sie stellt eine Methode `verbinde()` bereit, die eine Verbindung zur Datenbank aufbaut und zurückgibt.