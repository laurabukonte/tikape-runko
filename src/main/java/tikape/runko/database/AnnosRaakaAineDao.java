/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

/**
 *
 * @author LBUK
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE id = ?");
        // key is AnnosRaakaAine key!!
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer jarjestys = rs.getInt("jarjestys");
        Integer maara = rs.getInt("maara");
        String ohje = rs.getString("ohje");

        Integer raakaAineId = rs.getInt("raaka_aine_id");
        Integer annosId = rs.getInt("annos_id");

        AnnosRaakaAine r = new AnnosRaakaAine(id, annosId, raakaAineId, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return r;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void deleteRaakaAine(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE raaka_aine_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public void deleteAnnos(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public void addOne(String s) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Etsi annos_id:tä vastaavat annosRaakaAineet:
    public List<AnnosRaakaAine> findAnnokseenLiittyvat(Integer annosId) throws SQLException {
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        Connection connection = database.getConnection();

        // Find all annosRaakaAine where annos_id == annosId:
        PreparedStatement stmt = connection.prepareStatement("SELECT id, raaka_aine_id, jarjestys, maara, ohje  FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setInt(1, annosId);
        ResultSet rs = stmt.executeQuery();

        // Add annosRaakaAineet to a list:
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer raakaAineId = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            annosRaakaAineet.add(new AnnosRaakaAine(id, annosId, raakaAineId, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annosRaakaAineet;
    }

    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {
        AnnosRaakaAine ar = findByAnnosIdRaakaAineId(object.getAnnosId(), object.getRaakaAineId());

        if (ar != null) {
            return ar;
        }
            try (Connection connection = database.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO AnnosRaakaAine (annos_id, raaka_aine_id,  jarjestys, maara, ohje) "
                        + " VALUES (?, ?, ?, ?, ?)"
                );
                
                stmt.setInt(1, object.getAnnosId());
                stmt.setInt(2, object.getRaakaAineId());
                stmt.setInt(3, object.getJarjestys());
                stmt.setInt(4, object.getMaara());
                stmt.setString(5, object.getOhje());
                

                stmt.executeUpdate();
                stmt.close();
                connection.close();
            
        }

        return findByAnnosIdRaakaAineId(object.getAnnosId(), object.getRaakaAineId());
    }

    public AnnosRaakaAine findByAnnosIdRaakaAineId(Integer annosId, Integer raakaAineId) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, annos_id, raaka_aine_id,  jarjestys, maara, ohje  FROM AnnosRaakaAine WHERE annos_id = ? AND raaka_aine_id = ?");

            stmt.setInt(1, annosId);
            stmt.setInt(1, raakaAineId);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Integer id = rs.getInt("id");
            Integer annos_id = rs.getInt("annos_id");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            stmt.close();
            rs.close();
            connection.close();

            return new AnnosRaakaAine(id, annos_id, raaka_aine_id, jarjestys, maara, ohje);
        }
    }
}