package repository.jdbc;

import domain.Artist;
import repository.ArtistRepository;
import util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcArtistRepository implements ArtistRepository {

    @Override
    public int create(Artist artist) {
        String sql = "INSERT INTO artists(name, year_of_birth, nationality) VALUES(?, ?, ?) RETURNING id";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, artist.getName());
            ps.setInt(2, artist.getYearOfBirth());
            ps.setString(3, artist.getNationality());

            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            artist.setId(id);
            return id;

        } catch (SQLException e) {
            throw new RuntimeException("DB error (create artist): " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Artist> findById(int id) {
        String sql = "SELECT id, name, year_of_birth, nationality FROM artists WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            Artist a = new Artist(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("year_of_birth"),
                    rs.getString("nationality")
            );
            return Optional.of(a);

        } catch (SQLException e) {
            throw new RuntimeException("DB error (find artist): " + e.getMessage(), e);
        }
    }

    // NEW:
    @Override
    public List<Artist> findAll() {
        String sql = "SELECT id, name, year_of_birth, nationality FROM artists ORDER BY id";
        try (Connection conn = Db.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Artist> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Artist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("year_of_birth"),
                        rs.getString("nationality")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error (findAll artists): " + e.getMessage(), e);
        }
    }
}
