package repository;

import domain.Artwork;

import java.util.List;
import java.util.Optional;

public interface ArtworkRepository {
    int create(Artwork artwork);
    List<Artwork> findAll();
    Optional<Artwork> findById(int id);
    void updatePrice(int id, double newPrice);
    void updateSold(int id, boolean sold);
    void delete(int id);
}
