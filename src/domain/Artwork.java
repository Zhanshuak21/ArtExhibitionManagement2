package domain;

import exceptions.ValidationException;

import java.util.Objects;

public abstract class Artwork {
    private Integer id; // null до сохранения
    private String title;
    private int year;
    private double price;
    private boolean sold;
    private Artist artist;

    public Artwork(Integer id, String title, int year, double price, boolean sold, Artist artist) {
        setTitle(title);
        setYear(year);
        setPrice(price);
        setSold(sold);
        setArtist(artist);
        this.id = id;
    }

    public Artwork(String title, int year, double price, boolean sold, Artist artist) {
        this(null, title, year, price, sold, artist);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new ValidationException("Artwork title can't be empty");
        this.title = title.trim();
    }

    public int getYear() { return year; }
    public void setYear(int year) {
        if (year < 1000 || year > 2100) throw new ValidationException("Invalid artwork year: " + year);
        this.year = year;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new ValidationException("Price can't be negative");
        this.price = price;
    }

    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) {
        if (artist == null) throw new ValidationException("Artist can't be null");
        this.artist = artist;
    }

    public void markSold() { this.sold = true; }

    // Абстракция
    public abstract double calculateInsuranceCost();

    @Override
    public String toString() {
        return "Artwork{id=" + id + ", title='" + title + "', year=" + year + ", price=" + price + ", sold=" + sold +
                ", artist=" + (artist != null ? artist.getName() : "null") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artwork artwork)) return false;
        return year == artwork.year && Objects.equals(title, artwork.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }
}
