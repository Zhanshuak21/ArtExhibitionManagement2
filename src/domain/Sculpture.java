package domain;

public class Sculpture extends Artwork {
    private String medium;
    private double weightKg;

    public Sculpture(Integer id, String title, int year, double price, boolean sold, Artist artist, String medium, double weightKg) {
        super(id, title, year, price, sold, artist);
        this.medium = medium;
        this.weightKg = weightKg;
    }

    public Sculpture(String title, int year, double price, boolean sold, Artist artist, String medium, double weightKg) {
        this(null, title, year, price, sold, artist, medium, weightKg);
    }

    public String getMedium() { return medium; }
    public void setMedium(String medium) { this.medium = medium; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    @Override
    public double calculateInsuranceCost() {
        return (getPrice() * 0.005) + (weightKg * 100);
    }
}
