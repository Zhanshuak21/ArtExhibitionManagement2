package repository;

import domain.Artist;
import exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    int create(Artist artist);
    Optional<Artist> findById(int id);

    // NEW:
    List<Artist> findAll();

    default Artist requireById(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Artist not found: " + id));
    }
}
