package repository.jdbc;

import domain.Artist;
import domain.Artwork;
import repository.ArtistRepository;
import repository.ArtworkRepository;
import util.ArtworkFactory;
import util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcArtworkRepository implements ArtworkRepository {

    private final ArtistRepository artistRepository;

    public JdbcArtworkRepository(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public int create(Artwork artwork) {
        String sql = """
            INSERT INTO artworks(artist_id, title, year, price, type, material, style, medium, weight_kg, is_sold)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
            """;

        // RTTI пример
        String type = artwork.getClass().getSimpleName(); // Painting / Sculpture

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, artwork.getArtist().getId());
            ps.setString(2, artwork.getTitle());
            ps.setInt(3, artwork.getYear());
            ps.setDouble(4, artwork.getPrice());
            ps.setString(5, type);
            String material = null, style = null, medium = null;
            Double weight = null;

            if (artwork instanceof domain.Painting p) {
                material = p.getMaterial();
                style = p.getStyle();
            } else if (artwork instanceof domain.Sculpture s) {
                medium = s.getMedium();
                weight = s.getWeightKg();
            }

            ps.setString(6, material);
            ps.setString(7, style);
            ps.setString(8, medium);
            if (weight == null) ps.setNull(9, Types.DOUBLE);
            else ps.setDouble(9, weight);
            ps.setBoolean(10, artwork.isSold());

            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            artwork.setId(id);
            return id;

        } catch (SQLException e) {
            throw new RuntimeException("DB error (create artwork): " + e.getMessage(), e);
        }
    }

    @Override
    public List<Artwork> findAll() {
        String sql = "SELECT * FROM artworks ORDER BY id";
        try (Connection conn = Db.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Artwork> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error (findAll artworks): " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Artwork> findById(int id) {
        String sql = "SELECT * FROM artworks WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException("DB error (find artwork): " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePrice(int id, double newPrice) {
        String sql = "UPDATE artworks SET price = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("DB error (update price): " + e.getMessage(), e);
        }
    }

    @Override
    public void updateSold(int id, boolean sold) {
        String sql = "UPDATE artworks SET is_sold = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, sold);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("DB error (update sold): " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM artworks WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("DB error (delete artwork): " + e.getMessage(), e);
        }
    }

    private Artwork mapRow(ResultSet rs) throws SQLException {
        int artistId = rs.getInt("artist_id");
        Artist artist = artistRepository.requireById(artistId);

        String type = rs.getString("type");
        return ArtworkFactory.create(
                type,
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("year"),
                rs.getDouble("price"),
                rs.getBoolean("is_sold"),
                artist,
                rs.getString("material"),
                rs.getString("style"),
                rs.getString("medium"),
                (Double) rs.getObject("weight_kg")
        );
    }
}
