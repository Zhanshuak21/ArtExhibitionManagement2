import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== STARTING EXHIBITION DATA ENTRY ===");

        System.out.println("\n--- Enter Artist Details ---");
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine();

        System.out.print("Enter artist year of birth: ");
        int yearOfBirth = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter artist nationality: ");
        String nationality = scanner.nextLine();

        Artist userArtist = new Artist(name, yearOfBirth, nationality);
        System.out.println("-> Artist created: " + userArtist.getName());

        System.out.println("\n--- Enter Painting Details ---");
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
        System.out.println("-> Artwork created: " + userPainting.getTitle());


        Artist staticArtist = new Artist("Static Master X", 1700, "Dutch");
        Sculpture s1 = new Sculpture("Classic Bust", 1750, 950000.0, false, staticArtist, "Marble", 450.0);

        Painting p3 = new Painting("River View", 1880, 50000.0, false, staticArtist, "Watercolor", "Realist");
        Sculpture s4 = new Sculpture("Small Idol", 100, 100000.0, false, null, "Gold", 5.0);

        List<Artwork> batchUpload = new ArrayList<>();
        batchUpload.add(p3);
        batchUpload.add(s4);


        System.out.println("\n--- 2. Data Management and Overloading ---");

        Gallery cityExhibition = new Gallery("Annual City Showcase", "New York", 500);

        cityExhibition.addArtwork(userPainting);

        cityExhibition.addArtwork(batchUpload);

        cityExhibition.printInfo();

        System.out.println("\n--- 3. Sorting and Filtering ---");

        cityExhibition.sortByPrice();

        System.out.println("--- Sorted Artworks (Highest Price First) ---");
        for (Artwork artwork : cityExhibition.getExhibitionPieces()) {
            System.out.println(artwork.getTitle() + " - $" + String.format("%.2f", artwork.getPrice()));
        }

        s1.markSold();
        List<Artwork> soldWorks = cityExhibition.filterSoldArtworks();
        System.out.println("\nNumber of Sold Artworks: " + soldWorks.size());

        System.out.println("\n--- 4. Cleanup ---");
        scanner.close();
        System.out.println("Scanner closed successfully. Program finished.");
    }
}