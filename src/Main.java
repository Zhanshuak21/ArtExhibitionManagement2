import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1641j";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== ART GALLERY MANAGEMENT SYSTEM ===");

        while (running) {
            System.out.println("\nChoose an action:");
            System.out.println("1. List all artworks");
            System.out.println("2. Add artist and artwork");
            System.out.println("3. Update artwork price");
            System.out.println("4. Delete artwork");
            System.out.println("5. Find most expensive artwork");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    readArtworks();
                    break;
                case 2:
                    System.out.println("\n--- Artist Data ---");
                    System.out.print("Name: ");
                    String aName = scanner.nextLine();
                    System.out.print("Year of birth: ");
                    int aYear = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nationality: ");
                    String aNat = scanner.nextLine();

                    Artist artist = new Artist(aName, aYear, aNat);
                    int artistId = saveArtist(artist);

                    System.out.println("\n--- Artwork Data ---");
                    System.out.print("Title: ");
                    String pTitle = scanner.nextLine();
                    System.out.print("Year of creation: ");
                    int pYear = scanner.nextInt();
                    System.out.print("Price: ");
                    double pPrice = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Material: ");
                    String pMat = scanner.nextLine();
                    System.out.print("Style: ");
                    String pStyle = scanner.nextLine();
                    System.out.print("Is it sold? (true/false): ");
                    boolean isSold = scanner.nextBoolean();

                    Painting painting = new Painting(pTitle, pYear, pPrice, isSold, artist, pMat, pStyle);
                    saveArtwork(artistId, painting);
                    break;
                case 3:
                    readArtworks();
                    System.out.print("\nEnter ID of the artwork you want to change: ");
                    int idToUpdate = scanner.nextInt();
                    updateAnything(idToUpdate, scanner);
                    break;
                case 4:
                    readArtworks();
                    System.out.print("\nEnter ID to delete: ");
                    int delId = scanner.nextInt();
                    deleteArtwork(delId);
                    break;
                case 5:
                    findMostExpensiveArtwork();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Error: please choose a number between 0 and 5.");
            }
        }
        scanner.close();
    }

    public static int saveArtist(Artist artist) {
        String sql = "INSERT INTO artists(name, year_of_birth, nationality) VALUES(?, ?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artist.getName());
            pstmt.setInt(2, artist.getYearOfBirth());
            pstmt.setString(3, artist.getNationality());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Artist save error: " + e.getMessage());
        }
        return -1;
    }

    public static void saveArtwork(int artistId, Painting painting) {
        String sql = "INSERT INTO artworks(artist_id, title, year, price, type, material, style, is_sold) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, artistId);
            pstmt.setString(2, painting.getTitle());
            pstmt.setInt(3, painting.getYear());
            pstmt.setDouble(4, painting.getPrice());
            pstmt.setString(5, "Painting");
            pstmt.setString(6, painting.getMaterial());
            pstmt.setString(7, painting.getStyle());
            pstmt.setBoolean(8, painting.isSold());
            pstmt.executeUpdate();
            System.out.println("SUCCESS: Artwork added to database.");
        } catch (SQLException e) {
            System.err.println("Artwork save error: " + e.getMessage());
        }
    }

    public static void readArtworks() {
        String sql = "SELECT id, title, price, material, style, is_sold FROM artworks";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- CURRENT EXHIBITS ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Title: %s | Price: $%.2f | Material: %s | Style: %s | Sold: %b%n",
                        rs.getInt("id"), rs.getString("title"), rs.getDouble("price"),
                        rs.getString("material"), rs.getString("style"), rs.getBoolean("is_sold"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAnything(int id, Scanner scanner) {
        String selectSql = "SELECT * FROM artworks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- Editing Artwork ID: " + id + " ---");
                System.out.println("Current data: Title: " + rs.getString("title") +
                        ", Price: " + rs.getDouble("price") +
                        ", Sold: " + rs.getBoolean("is_sold") +
                        "  Material: " + rs.getString("material") +
                        ", Style: " + rs.getString("style"));

                System.out.println("\nWhich field to change? (title, price, material, style, is_sold)");
                System.out.print("Field name: ");
                scanner.nextLine();
                String field = scanner.nextLine().toLowerCase();

                System.out.print("Enter new value (or press Enter to keep old): ");
                String newValue = scanner.nextLine();

                if (newValue.isEmpty()) {
                    System.out.println("No changes made.");
                    return;
                }

                String updateSql = "UPDATE artworks SET " + field + " = ? WHERE id = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

                    if (field.equals("price")) {
                        updatePstmt.setDouble(1, Double.parseDouble(newValue));
                    } else if (field.equals("is_sold")) {
                        updatePstmt.setBoolean(1, Boolean.parseBoolean(newValue));
                    } else if (field.equals("year")) {
                        updatePstmt.setInt(1, Integer.parseInt(newValue));
                    } else {
                        updatePstmt.setString(1, newValue);
                    }

                    updatePstmt.setInt(2, id);
                    updatePstmt.executeUpdate();
                    System.out.println("SUCCESS: Field '" + field + "' updated to '" + newValue + "'");
                }

            } else {
                System.out.println("ERROR: Artwork with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
        }
    }

    public static void deleteArtwork(int id) {
        String sql = "DELETE FROM artworks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("SUCCESS: Artwork deleted from database.");
            else System.out.println("ERROR: Deletion error, check the ID.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findMostExpensiveArtwork() {
        String sql = "SELECT title, price FROM artworks ORDER BY price DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("\nMOST EXPENSIVE OBJECT: " + rs.getString("title") + " ($" + rs.getDouble("price") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}