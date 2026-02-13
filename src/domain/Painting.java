package domain;

public class Painting extends Artwork {
    private String material;
    private String style;

    public Painting(Integer id, String title, int year, double price, boolean sold, Artist artist, String material, String style) {
        super(id, title, year, price, sold, artist);
        this.material = material;
        this.style = style;
    }

    public Painting(String title, int year, double price, boolean sold, Artist artist, String material, String style) {
        this(null, title, year, price, sold, artist, material, style);
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    @Override
    public double calculateInsuranceCost() {
        return getPrice() * 0.01;
    }
}
