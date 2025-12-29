import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Gallery {
    private String name;
    private String city;
    private int maxCapacity;
    private final List<Artwork> exhibitionPieces;

    public Gallery(String name, String city, int maxCapacity) {
        this.name = name;
        this.city = city;
        this.maxCapacity = maxCapacity;
        this.exhibitionPieces = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    public List<Artwork> getExhibitionPieces() {
        return exhibitionPieces;
    }

    public void addArtwork(Artwork artwork) {
        if (artwork != null) {
            this.exhibitionPieces.add(artwork);
            System.out.println("Added: " + artwork.getTitle());
        }
    }

    public void addArtwork(List<Artwork> artworks) {
        if (artworks != null) {
            int count = 0;
            for (Artwork artwork : artworks) {
                if (artwork != null) {
                    this.exhibitionPieces.add(artwork);
                    count++;
                }
            }
            System.out.println("Added " + count + " artworks from the list.");
        }
    }

    public void sortByPrice() {
        Collections.sort(exhibitionPieces, Comparator.comparing(Artwork::getPrice).reversed());
        System.out.println("--- Artworks sorted by price (highest first) ---");
    }

    public List<Artwork> filterSoldArtworks() {
        List<Artwork> soldList = new ArrayList<>();

        for (int i = 0; i < exhibitionPieces.size(); i++) {
            Artwork currentArtwork = exhibitionPieces.get(i);

            if (currentArtwork.isSold()) {
                soldList.add(currentArtwork);
            }
        }
        return soldList;
    }

    public Artwork findMostExpensive() {
        if (exhibitionPieces.isEmpty()) {
            return null;
        }

        Artwork mostExpensive = exhibitionPieces.get(0);

        for (int i = 1; i < exhibitionPieces.size(); i++) {
            Artwork currentArtwork = exhibitionPieces.get(i);

            if (currentArtwork.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = currentArtwork;
            }
        }
        return mostExpensive;
    }

    public void printInfo() {
        System.out.println("Gallery Name: " + name + ", Total Pieces: " + exhibitionPieces.size());
    }
}