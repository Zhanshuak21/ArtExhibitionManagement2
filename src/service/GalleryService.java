package service;

import domain.Artist;
import domain.Artwork;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import repository.ArtistRepository;
import repository.ArtworkRepository;

import java.util.List;

public class GalleryService {
    private final ArtistRepository artistRepo;
    private final ArtworkRepository artworkRepo;

    // DIP
    public GalleryService(ArtistRepository artistRepo, ArtworkRepository artworkRepo) {
        this.artistRepo = artistRepo;
        this.artworkRepo = artworkRepo;
    }

    public int createArtist(Artist artist) {
        return artistRepo.create(artist);
    }

    // NEW:
    public List<Artist> listArtists() {
        return artistRepo.findAll();
    }

    // NEW:
    public Artist getArtist(int id) {
        return artistRepo.findById(id).orElseThrow(() -> new NotFoundException("Artist not found: " + id));
    }

    public int createArtwork(Artwork artwork) {
        if (artwork.getArtist() == null || artwork.getArtist().getId() == null) {
            throw new ValidationException("artistId is required");
        }
        // Проверяем что artist реально существует
        Artist realArtist = getArtist(artwork.getArtist().getId());
        artwork.setArtist(realArtist);

        return artworkRepo.create(artwork);
    }

    public List<Artwork> listArtworks() {
        return artworkRepo.findAll();
    }

    public Artwork getArtwork(int id) {
        return artworkRepo.findById(id).orElseThrow(() -> new NotFoundException("Artwork not found: " + id));
    }

    public void changePrice(int id, double newPrice) {
        if (newPrice < 0) throw new ValidationException("Price can't be negative");
        getArtwork(id);
        artworkRepo.updatePrice(id, newPrice);
    }

    public void setSold(int id, boolean sold) {
        getArtwork(id);
        artworkRepo.updateSold(id, sold);
    }

    public void deleteArtwork(int id) {
        getArtwork(id);
        artworkRepo.delete(id);
    }
}
