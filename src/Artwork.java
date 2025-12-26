import java.util.Objects;

public abstract class Artwork {
    private String title;
    private int year;
    private double price;
    private boolean isSold;
    private Artist artist;

    public Artwork() {
    }

    public Artwork(String title, int year, double price, boolean isSold, Artist artist) {
        this.title = title;
        this.year = year;
        this.price = price;
        this.isSold = isSold;
        this.artist = artist;
    }

    public abstract double calculateInsuranceCost();

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }
    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }

    public void markSold() {
        this.isSold = true;
        System.out.println("Status update: " + this.title + " is now SOLD.");
    }

    public void printInfo() {
        String artistName = "Unknown Artist";
        if (artist != null) {
            artistName = artist.getName();
        }

        System.out.println("Artwork Title: " + title +
                " | Artist: " + artistName +
                " | Year: " + year +
                " | Price: $" + String.format("%.2f", price) +
                " | Sold: " + isSold);
    }

    @Override
    public String toString() {
        String artistName = (artist != null) ? artist.getName() : "Unknown";
        return "Artwork [Title: " + title + ", Artist: " + artistName + ", Price: $" + String.format("%.2f", price) + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artwork artwork = (Artwork) o;
        return year == artwork.year && Objects.equals(title, artwork.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }
}