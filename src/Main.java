import api.ArtGalleryHttpServer;
import repository.ArtistRepository;
import repository.ArtworkRepository;
import repository.jdbc.JdbcArtistRepository;
import repository.jdbc.JdbcArtworkRepository;
import service.GalleryService;

public class Main {
    public static void main(String[] args) throws Exception {
        ArtistRepository artistRepo = new JdbcArtistRepository();
        ArtworkRepository artworkRepo = new JdbcArtworkRepository(artistRepo);

        GalleryService service = new GalleryService(artistRepo, artworkRepo);

        new ArtGalleryHttpServer(service).start(8080);
    }
}

