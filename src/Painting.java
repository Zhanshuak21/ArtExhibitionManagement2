public class Painting extends Artwork {
    private String material;
    private String style;

    public Painting(String title, int year, double price, boolean isSold, Artist artist, String material, String style) {
        super(title, year, price, isSold, artist);
        this.material = material;
        this.style = style;
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    @Override
    public double calculateInsuranceCost() {
        return this.getPrice() * 0.01;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("  -> SPECIFIC INFO: Type: Painting | Material: " + material + " | Style: " + style);
        System.out.println("  -> Insurance Cost: $" + String.format("%.2f", calculateInsuranceCost()));
    }
}