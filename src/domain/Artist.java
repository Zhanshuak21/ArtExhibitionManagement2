package domain;

import exceptions.ValidationException;

import java.util.Objects;

public class Artist {
    private Integer id; // может быть null до сохранения в БД
    private String name;
    private int yearOfBirth;
    private String nationality;

    public Artist(Integer id, String name, int yearOfBirth, String nationality) {
        setName(name);
        setYearOfBirth(yearOfBirth);
        setNationality(nationality);
        this.id = id;
    }

    public Artist(String name, int yearOfBirth, String nationality) {
        this(null, name, yearOfBirth, nationality);
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Integer yearOfBirth;
        private String nationality;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder yearOfBirth(int year) {
            this.yearOfBirth = year;
            return this;
        }

        public Builder nationality(String nat) {
            this.nationality = nat;
            return this;
        }

        public Artist build() {
            if (yearOfBirth == null) throw new ValidationException("Artist.yearOfBirth is required");
            return new Artist(id, name, yearOfBirth, nationality);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new ValidationException("Artist name can't be empty");
        this.name = name.trim();
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        if (yearOfBirth < 1000 || yearOfBirth > 2100)
            throw new ValidationException("Invalid yearOfBirth: " + yearOfBirth);
        this.yearOfBirth = yearOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        if (nationality == null || nationality.trim().isEmpty())
            throw new ValidationException("Nationality can't be empty");
        this.nationality = nationality.trim();
    }

    @Override
    public String toString() {
        return "Artist{id=" + id + ", name='" + name + "', yearOfBirth=" + yearOfBirth + ", nationality='" + nationality + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist artist)) return false;
        return yearOfBirth == artist.yearOfBirth && Objects.equals(name, artist.name) && Objects.equals(nationality, artist.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, yearOfBirth, nationality);
    }
}
