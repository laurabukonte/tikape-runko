package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
          
        
        lista.add("DROP TABLE IF EXISTS Smoothie;");
        lista.add("DROP TABLE IF EXISTS Annos;");
        lista.add("DROP TABLE IF EXISTS RaakaAine;");
        lista.add("DROP TABLE IF EXISTS AnnosRaakaAine;");
       
     
        lista.add("CREATE TABLE Smoothie (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE Annos (id integer PRIMARY KEY, nimi varchar(255));");
        lista.add("CREATE TABLE RaakaAine (id integer PRIMARY KEY, nimi varchar(255));");
        
        lista.add("CREATE TABLE AnnosRaakaAine "
                + "(id integer," 
                + "annos_id integer,"
                + "raaka_aine_id integer,"
                + "jarjestys integer, "
                + "maara double, "
                + "ohje varchar(2500),"
                + "FOREIGN KEY (annos_id) REFERENCES Annos(id),"
                + "FOREIGN KEY (raaka_aine_id) REFERENCES RaakaAine(id));");

        
        
       // lista.add("CREATE TABLE AnnosRaakaAine (id integer PRIMARY KEY, annos_id integer, raaka_aine_id integer, jarjestys integer, maara integer, ohje varchar(2500));");
       
        
      //  lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Platon');");
      //  lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Aristoteles');");
      //  lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Homeros');");

        return lista;
    }
}
