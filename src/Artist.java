import java.util.Objects;

public class Artist {
    private String name;
    private int yearOfBirth;
    private String nationality;

    public Artist() {
    }

    public Artist(String name, int yearOfBirth, String nationality) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getYearOfBirth() {
        return yearOfBirth;
    }
    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void printInfo() {
        System.out.println("Artist Name: " + name + ", Born: " + yearOfBirth + ", Nationality: " + nationality);
    }

    @Override
    public String toString() {
        return "Artist [Name: " + name + ", Born: " + yearOfBirth + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return yearOfBirth == artist.yearOfBirth && Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, yearOfBirth);
    }
}