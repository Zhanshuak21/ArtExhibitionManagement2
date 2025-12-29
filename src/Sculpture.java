public class Sculpture extends Artwork {
    private String medium;
    private double weightKg;

    public Sculpture(String title, int year, double price, boolean isSold, Artist artist, String medium, double weightKg) {
        super(title, year, price, isSold, artist);
        this.medium = medium;
        this.weightKg = weightKg;
    }

    public String getMedium() {
        return medium;
    }
    public void setMedium(String medium) {
        this.medium = medium;
    }
    public double getWeightKg() {
        return weightKg;
    }
    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    @Override
    public double calculateInsuranceCost() {
        return (this.getPrice() * 0.005) + (weightKg * 100);
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("  -> SPECIFIC INFO: Type: Sculpture | Medium: " + medium + " | Weight: " + weightKg + "kg");
        System.out.println("  -> Insurance Cost: $" + String.format("%.2f", calculateInsuranceCost()));
    }
}