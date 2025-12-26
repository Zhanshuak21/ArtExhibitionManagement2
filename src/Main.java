import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CREATE NEW ARTIST ===");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine();

        System.out.print("Enter artist year of birth: ");
        int yearOfBirth = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter artist nationality: ");
        String nationality = scanner.nextLine();

        Artist userArtist = new Artist(name, yearOfBirth, nationality);
        System.out.println("-> Artist created: " + userArtist.getName() + "\n");


        System.out.println("=== CREATE NEW PAINTING ===");
        System.out.print("Enter artwork title: ");
        String title = scanner.nextLine();

        System.out.print("Enter year of creation: ");
        int year = scanner.nextInt();

        System.out.print("Enter price (in dollars): ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter material (e.g., 'Oil on Canvas'): ");
        String material = scanner.nextLine();

        System.out.print("Enter style (e.g., 'Abstract'): ");
        String style = scanner.nextLine();

        Painting userPainting = new Painting(title, year, price, false, userArtist, material, style);

        System.out.println("-> Artwork created: " + userPainting.getTitle() + "\n");

        scanner.close();


        Artist staticArtist = new Artist("Static Master X", 1700, "Dutch");
        Sculpture s1 = new Sculpture("Classic Bust", 1750, 950000.0, false, staticArtist, "Marble", 450.0);


        System.out.println("--- Polymorphism Demonstration (printInfo) ---");
        userPainting.printInfo();
        s1.printInfo();


        System.out.println("\n--- 4. Data Pool Demonstration (Gallery) ---");

        Gallery cityExhibition = new Gallery("Annual City Showcase", "New York", 500);

        cityExhibition.addArtwork(userPainting);
        cityExhibition.addArtwork(s1);

        cityExhibition.printInfo();

        System.out.println("\n--- 5. Sorting and Searching ---");

        cityExhibition.sortByPrice();

        for (Artwork artwork : cityExhibition.getExhibitionPieces()) {
            String authorName = (artwork.getArtist() != null) ? artwork.getArtist().getName() : "Unknown";
            System.out.println(artwork.getTitle() + " (Author: " + authorName + ") - $" + String.format("%.2f", artwork.getPrice()));
        }

        Artwork mostExpensive = cityExhibition.findMostExpensive();
        if (mostExpensive != null) {
            System.out.println("\nMost Expensive Artwork: " + mostExpensive.getTitle());
        }

        userPainting.markSold();
        List<Artwork> soldWorks = cityExhibition.filterSoldArtworks();

        System.out.println("\n--- Filtering: Sold Artworks (" + soldWorks.size() + ") ---");
        for (Artwork artwork : soldWorks) {
            System.out.println(artwork.getTitle() + " (Status: " + artwork.isSold() + ")");
        }


        System.out.println("\n--- 6. toString() and equals() Demonstration ---");

        System.out.println("Painting toString: " + userPainting);

        Artist artistCopy = new Artist(userArtist.getName(), userArtist.getYearOfBirth(), "France");
        System.out.println("Comparison: userArtist.equals(artistCopy): " + userArtist.equals(artistCopy));
    }
}