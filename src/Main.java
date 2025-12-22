public class Main {
    public static void main(String[] args) {
        Artist artist1 = new Artist("Da Vinci", 1455, "Italy");
        Artist artist2 = new Artist("Picasso", 1881, "Spanish");

        Artwork painting1 = new Artwork("Water Lilies", 1915, 45000000.0, false);
        Artwork sculpture1 = new Artwork("Gernica", 1880, 15000000.0, false);

        Gallery gallery = new Gallery("Louvre Exhibition", "Paris", 500);

        System.out.println("=== EXHIBITION STATUS ===");
        artist1.printInfo();
        artist2.printInfo();
        painting1.printInfo();
        sculpture1.printInfo();
        gallery.printInfo();

        painting1.markSold();
        painting1.printInfo();

        System.out.println("\n=== ARTWORK PRICE COMPARISON ===");

        int comparison = painting1.compareByPrice(sculpture1);

        if (comparison > 0) {
            System.out.println(painting1.getTitle() + " is more expensive than " + sculpture1.getTitle() + ".");
        } else if (comparison < 0) {
            System.out.println(painting1.getTitle() + " is less expensive than " + sculpture1.getTitle() + ".");
        } else {
            System.out.println(painting1.getTitle() + " and " + sculpture1.getTitle() + " cost the same.");
        }

        System.out.println("\n=== LOOP THROUGH ARTWORKS ===");

        Artwork[] artworks = { painting1, sculpture1 };

        double totalValue = 0.0;
        Artwork mostExpensive = null;

        for (int i = 0; i < artworks.length; i++) {
            Artwork currentArtwork = artworks[i];

            System.out.println("Piece " + (i + 1) + ": " + currentArtwork.getTitle() + " priced at $" + currentArtwork.getPrice());

            totalValue += currentArtwork.getPrice();

            if (mostExpensive == null || currentArtwork.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = currentArtwork;
            }
        }

        System.out.println("Total value of displayed artworks: $" + totalValue);
        System.out.println("Most expensive artwork: " + mostExpensive.getTitle());
    }
}
