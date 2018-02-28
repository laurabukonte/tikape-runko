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
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer>{
    
    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine r = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return r;
    }
    
    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        
        stmt.setInt(1, key);
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }

    @Override
    public void addOne(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        
        stmt.setString(1, nimi);
        stmt.executeUpdate();
        
        stmt.close();
        connection.close();
    }
    
    public List<RaakaAine> findRaakaAineByAnnos(Integer annosId) throws SQLException {
        String query = "SELECT RaakaAine.id, RaakaAine.nimi FROM RaakaAine, AnnosRaakaAine, Annos\n"
                        +"     WHERE RaakaAine.id = AnnosRaakaAine.raaka_aine_id"
                        +"     AND Annos.id = AnnosRaakaAine.annos_id"
                        +"     AND Annos.id = ?";
        List<RaakaAine> raakaAineet = new ArrayList<>();
        
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, annosId);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                raakaAineet.add(new RaakaAine(rs.getInt("id"), rs.getString("nimi")));
            }
            
            rs.close();
            stmt.close();
            connection.close();
        }
        
        return raakaAineet;
    }
    
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        RaakaAine ra = findByName(object.getNimi());
        
        if (ra != null) {
            return ra;
        }

        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
            
            stmt.close();
            connection.close();
        }

        return findByName(object.getNimi());
    }
    
    private RaakaAine findByName(String name) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE nimi = ?");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            
            rs.close();
            stmt.close();
            connection.close();

            return new RaakaAine(id, nimi);
        }
    }


}